package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.out.websocket.message.NextRoundMessage;
import org.pokerino.backend.adapter.out.websocket.message.StartGameMessage;
import org.pokerino.backend.application.port.in.game.GameUseCase;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.application.port.out.GameNotificationPort;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.GameState;
import org.pokerino.backend.domain.game.PokerGame;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class GameService implements GameUseCase {
    final TurnUseCase turnUseCase;
    final ManageGamePort manageGamePort;
    final LoadUserPort loadUserPort;
    final SaveUserPort saveUserPort;
    final GameNotificationPort gameNotificationPort;
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    ScheduledFuture<?> currentTask;
    int countdown;

    @Override
    public void win(final PokerGame game, final GamePlayer player) {
        // stops all schedulers and cleans the game
        // rolls out the exp rewards and the prize pool
    }

    // inherits the scheduler while a round is running
    @Override
    public void nextTurn(PokerGame game) {
        // Cancel any previous scheduler for safety
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
        countdown = 0;
        final int turnTime = game.getOptions().getTurnTime();
        final GamePlayer current = game.getCurrent();

        currentTask = scheduler.scheduleAtFixedRate(() -> {
            countdown++;

            // Call rememberTurn every 5 seconds, but not at the end
            if (countdown % 5 == 0 && countdown < turnTime) {
                rememberTurn(game);
            }

            if (countdown >= turnTime) {
                // Time's up: cancel the scheduler and fold the player
                currentTask.cancel(false); // Cancel this task
                turnUseCase.fold(game, current);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    // inherits the scheduler while one round is ending until the start of the next round
    @Override
    public void endBettingRound(PokerGame game) {

    }

    // inherits the scheduler while game is running
    @Override
    public void rememberTurn(PokerGame game) {
        final GamePlayer current = game.getCurrent();

        // TODO: Find available actions and send a message
    }

    @Override
    public void startScheduler(PokerGame game) {
        countdown = -1; // Set countdown one below zero, since no 5 seconds have passed yet
        tryStart(game);
    }

    // inherits the scheduler before game starts
    @Override
    public void tryStart(PokerGame game) {
        // This game is already started, no need to retry
        if (game.getState() != GameState.WAITING_FOR_PLAYERS) {
            return;
        }

        // If the game is full, start it immediately
        int playerCount = game.playerCount();
        if (playerCount >= game.getOptions().getMaxPlayers()) {
            startGame(game);
            return;
        }

        // Increment the countdown
        countdown++;

        // If the game has less than 2 players, reset the countdown
        if (game.playerCount() < 2) {
            countdown = 0;
        }

        // If the countdown reaches 6 (30 seconds), start the game
        if (countdown >= 6) {
            startGame(game);
            return;
        }

        // If can't start the game, schedule a retry
        scheduler.schedule(() -> {
            tryStart(game);
        }, 5, TimeUnit.SECONDS);
    }

    @Override
    public void startGame(PokerGame game) {
        game.setState(GameState.IN_ROUND);
        StartGameMessage message = new StartGameMessage(game.playerCount());
        gameNotificationPort.notifyStartGame(game.getGameCode(), message);
        nextRound(game); // Start the first round of the game
        nextTurn(game); // Notify the game service that the first turn started
    }

    @Override
    public void nextRound(PokerGame game) {
        game.moveDealer(); // Move the dealer to the next player

        // Reset the cards on the table & players
        game.resetCards();
        game.resetParticipants();

        // Calculate who pays the blinds and take them in
        long smallBlind = game.getOptions().getSmallBlind();
        long bigBlind = smallBlind * 2;

        game.moveCurrent(); // Move to small blind

        GamePlayer smallBlindPlayer = game.getSmallBlind();
        smallBlindPlayer.setChips(smallBlindPlayer.getChips() - smallBlind);
        smallBlindPlayer.setBet(smallBlindPlayer.getBet() + smallBlind);
        game.moveCurrent(); // Move to big blind

        GamePlayer bigBlindPlayer = game.getBigBlind();
        bigBlindPlayer.setChips(bigBlindPlayer.getChips() - bigBlind);
        bigBlindPlayer.setBet(bigBlindPlayer.getBet() + bigBlind);
        game.moveCurrent(); // Move to the next player after big blind (could also be small blind)

        // At this point, the game is ready to start!
        NextRoundMessage message = new NextRoundMessage(
                smallBlindPlayer.getUsername(),
                smallBlind,
                bigBlindPlayer.getUsername(),
                bigBlind
        );
        gameNotificationPort.notifyNextRound(game.getGameCode(), message);
    }
}
