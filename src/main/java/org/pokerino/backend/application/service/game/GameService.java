package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.game.ActionsResponse;
import org.pokerino.backend.adapter.out.websocket.message.*;
import org.pokerino.backend.adapter.out.websocket.message.log.ActionMessage;
import org.pokerino.backend.application.port.in.game.FindStrongestHandUseCase;
import org.pokerino.backend.application.port.in.game.GameUseCase;
import org.pokerino.backend.application.port.in.game.GetGameUseCase;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.application.port.out.GameNotificationPort;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.game.Action;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.GameState;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class GameService implements GameUseCase {
    final ManageGamePort manageGamePort;
    final LoadUserPort loadUserPort;
    final SaveUserPort saveUserPort;
    final GameNotificationPort gameNotificationPort;
    final GetGameUseCase getGameUseCase;
    final FindStrongestHandUseCase findStrongestHandUseCase;
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    ScheduledFuture<?> currentTask;
    int countdown;

    @Override
    public void win(final PokerGame game, final GamePlayer player) {
        // Stop the running scheduler for this game
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }

        final long prize = game.getTotalChips();
        final FinishGameMessage finishGameMessage = new FinishGameMessage(
                player.getUsername(),
                prize
        );

        final User user = loadUserPort.findByUsername(player.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found: " + player.getUsername()));

        user.setChips(user.getChips() + prize);

        // TODO: Set exp for different players -> need a mechanism for that! For now just give 1000 exp to winner
        user.setExperience(user.getExperience() + 1000);

        saveUserPort.saveUser(user); // store the user with the changes
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
            } else if (countdown >= turnTime) {
                // Time's up: cancel the scheduler and fold the player
                currentTask.cancel(false); // Cancel this task
                current.setFolded(true);
                if (game.moveCurrent()) { // The betting round is over, proceed the game
                    endBettingRound(game); // either end the round or add new cards to table
                } else {
                    final ActionMessage actionMessage = new ActionMessage(
                             current.getUsername(),
                            Action.FOLD,
                            0,
                            game.getCurrentBet(),
                            false
                    );
                    gameNotificationPort.notifyAction(game.getGameCode(), actionMessage);
                    nextTurn(game);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    // inherits the scheduler while one round is ending until the start of the next round
    @Override
    public void endBettingRound(PokerGame game) {
        final int cards = game.openCards();
        final var temp = game.getCardsOnTable();
        if (cards == 5) { // Now we need to determine the winner!
            // count the pot
            long pot = 0;
            for (final GamePlayer participant : game.getParticipants()) {
                pot += participant.getBet();
            }

            // Find the winners
            final List<GamePlayer> winners = findStrongestHandUseCase.findStrongestHands(game);

            // Share the chips among the winners
            long share = pot / winners.size();
            long remainder = pot % winners.size();

            for (int i = 0; i < winners.size(); i++) {
                GamePlayer winner = winners.get(i);
                long chipsToAdd = share + (i < remainder ? 1 : 0); // Distribute remainder
                winner.setChips(winner.getChips() + chipsToAdd);
            }

            // End Round message
            final Map<String, List<String>> showdownCards = new HashMap<>();
            for (final GamePlayer participant : game.getParticipants()) {
                if (participant.isDead() || participant.isFolded()) {
                    continue;
                }
                showdownCards.put(participant.getUsername(), Arrays.asList(participant.getHand()));
            }
            final EndRoundMessage endRoundMessage = new EndRoundMessage(
                    winners.stream().map(GamePlayer::getUsername).toList(),
                    pot,
                    showdownCards
            );
            gameNotificationPort.notifyEndRound(game.getGameCode(), endRoundMessage);

            // Kill the players that have no chips left
            for (GamePlayer participant : game.getParticipants()) {
                if (participant.getChips() <= 0 && !participant.isDead()) {
                    participant.setDead(true);
                }
            }

            // Check if only one player remains in the game -> We can end it here!
            final int count = game.aliveCount();
            if (count == 1) {
                // Last player won, handle and then delete the game.
                win(game, game.getCurrent());
            } else if (count == 0) { // No players left, remove the game
                manageGamePort.removeGame(game.getGameCode());
            } else {
                nextRound(game); // Start the next round
            }
        } else if (cards == 4) { // Uncover one more card
            game.serveCards(1);
            final List<String> allCards = List.of(temp[0], temp[1], temp[2], temp[3], temp[4]);
            final List<String> newCards = List.of(temp[4]);
            final NewCardsMessage message = new NewCardsMessage(
                    allCards,
                    newCards
            );
            gameNotificationPort.notifyNewCards(game.getGameCode(), message);
            nextRound(game); // Start the first round of the game
            nextTurn(game); // Notify the game service that the first turn started
        } else if (cards == 3) { // Uncover one more card
            game.serveCards(1);
            final List<String> allCards = List.of(temp[0], temp[1], temp[2], temp[3]);
            final List<String> newCards = List.of(temp[3]);
            final NewCardsMessage message = new NewCardsMessage(
                    allCards,
                    newCards
            );
            gameNotificationPort.notifyNewCards(game.getGameCode(), message);
            nextRound(game); // Start the first round of the game
            nextTurn(game); // Notify the game service that the first turn started
        } else { // Uncover 3 cards
            game.serveCards(3);
            final List<String> allCards = List.of(temp[0], temp[1], temp[2]);
            final List<String> newCards = List.of(temp[0], temp[1], temp[2]);
            final NewCardsMessage message = new NewCardsMessage(
                    allCards,
                    newCards
            );
            gameNotificationPort.notifyNewCards(game.getGameCode(), message);
            nextRound(game); // Start the first round of the game
            nextTurn(game); // Notify the game service that the first turn started
        }
    }

    // inherits the scheduler while game is running
    @Override
    public void rememberTurn(PokerGame game) {
        final GamePlayer current = game.getCurrent();
        final ActionsResponse actions = getGameUseCase.availableActions(game, current.getUsername());
        final List<Action> available = actions.actions();
        final TurnMessage turnMessage = new TurnMessage(
                current.getUsername(),
                available,
                game.getCurrentBet(),
                countdown,
                game.getOptions().getTurnTime()
        );
        gameNotificationPort.notifyTurn(game.getGameCode(), turnMessage);
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