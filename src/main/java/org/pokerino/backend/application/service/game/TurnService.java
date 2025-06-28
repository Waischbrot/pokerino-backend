package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.out.websocket.message.log.ActionMessage;
import org.pokerino.backend.application.port.in.game.GameUseCase;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.application.port.out.GameNotificationPort;
import org.pokerino.backend.domain.game.Action;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TurnService implements TurnUseCase {
    GameUseCase gameUseCase;
    GameNotificationPort gameNotificationPort;

    @Override
    public void fold(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        player.setFolded(true);
        if (game.moveCurrent()) { // The betting round is over, proceed the game
            gameUseCase.endBettingRound(game); // either end the round or add new cards to table
        } else {
            final ActionMessage actionMessage = new ActionMessage(
                    player.getUsername(),
                    Action.FOLD,
                    0,
                    game.getCurrentBet(),
                    false
            );
            gameNotificationPort.notifyAction(game.getGameCode(), actionMessage);
            gameUseCase.nextTurn(game); // Notifies the game service that another turn started
        }
    }

    @Override
    public void check(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        if (game.getCurrent().getChips() < game.getCurrent().getBet()) {
            gameUseCase.rememberTurn(game); // Remember the turn for this player
            throw new BadRequestException("You cannot check, you must call or raise");
        }
        if (game.moveCurrent()) { // The betting round is over, proceed the game
            gameUseCase.endBettingRound(game); // either end the round or add new cards to table
        } else {
            final ActionMessage actionMessage = new ActionMessage(
                    player.getUsername(),
                    Action.CHECK,
                    0,
                    game.getCurrentBet(),
                    false
            );
            gameNotificationPort.notifyAction(game.getGameCode(), actionMessage);
            gameUseCase.nextTurn(game); // Notifies the game service that another turn started
        }
    }

    @Override
    public void call(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        long toCall = game.getCurrentBet() - player.getBet();
        if (toCall > player.getChips()) {
            gameUseCase.rememberTurn(game); // Remember the turn for this player
            throw new BadRequestException("You do not have enough chips to call");
        }
        player.setChips(player.getChips() - toCall);
        player.setBet(player.getBet() + toCall);
        if (game.moveCurrent()) { // The betting round is over, proceed the game
            gameUseCase.endBettingRound(game); // either end the round or add new cards to table
        } else {
            final ActionMessage actionMessage = new ActionMessage(
                    player.getUsername(),
                    Action.CALL,
                    toCall,
                    game.getCurrentBet(),
                    false
            );
            gameNotificationPort.notifyAction(game.getGameCode(), actionMessage);
            gameUseCase.nextTurn(game); // Notifies the game service that another turn started
        }
    }

    @Override
    public void raise(PokerGame game, GamePlayer player, long chips) {
        checkTurn(game, player);
        long toCall = game.getCurrentBet() - player.getBet();
        long minRaise = game.getMinRaise();

        if (chips < toCall + minRaise) {
            gameUseCase.rememberTurn(game); // Remember the turn for this player
            throw new BadRequestException("Raise must be at least the minimum raise above the current bet");
        }
        if (chips > player.getChips()) {
            gameUseCase.rememberTurn(game); // Remember the turn for this player
            throw new BadRequestException("You do not have enough chips to raise");
        }

        player.setChips(player.getChips() - chips);
        player.setBet(player.getBet() + chips);

        if (game.moveCurrent()) { // The betting round is over, proceed the game
            gameUseCase.endBettingRound(game); // either end the round or add new cards to table
        } else {
            final ActionMessage actionMessage = new ActionMessage(
                    player.getUsername(),
                    Action.RAISE,
                    chips,
                    game.getCurrentBet(),
                    false
            );
            gameNotificationPort.notifyAction(game.getGameCode(), actionMessage);
            gameUseCase.nextTurn(game); // Notifies the game service that another turn started
        }
    }

    @Override
    public void allIn(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        long allInAmount = player.getChips(); // For the notification
        player.setBet(player.getBet() + player.getChips());
        player.setChips(0);

        if (game.moveCurrent()) { // The betting round is over, proceed the game
            gameUseCase.endBettingRound(game); // either end the round or add new cards to table
        } else {
            final ActionMessage actionMessage = new ActionMessage(
                    player.getUsername(),
                    Action.ALL_IN,
                    allInAmount,
                    game.getCurrentBet(),
                    true
            );
            gameNotificationPort.notifyAction(game.getGameCode(), actionMessage);
            gameUseCase.nextTurn(game); // Notifies the game service that another turn started
        }
    }

    private void checkTurn(PokerGame game, GamePlayer player) {
        if (!game.getCurrent().equals(player)) {
            throw new BadRequestException("It's not your turn");
        }
    }
}