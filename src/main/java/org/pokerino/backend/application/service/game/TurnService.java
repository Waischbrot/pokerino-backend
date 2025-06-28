package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.application.port.in.game.GameUseCase;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.application.port.out.GameNotificationPort;
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
            // TODO: Implement logic for this here
        } else {
            // TODO: Send the action message and the turn message for the next player
        }
    }

    @Override
    public void check(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        if (game.getCurrent().getChips() < game.getCurrent().getBet()) {
            // TODO: Send another turn message for this player
            throw new BadRequestException("You cannot check, you must call or raise");
        }
        if (game.moveCurrent()) { // The betting round is over, proceed the game
            // TODO: Implement logic for this here
        } else {
            // TODO: Send the action message and the turn message for the next player
        }
    }

    @Override
    public void call(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        long toCall = game.getCurrentBet() - player.getBet();
        if (toCall > player.getChips()) {
            // TODO: Send another turn message for this player
            throw new BadRequestException("You do not have enough chips to call");
        }
        player.setChips(player.getChips() - toCall);
        player.setBet(player.getBet() + toCall);
        if (game.moveCurrent()) { // The betting round is over, proceed the game
            // TODO: Implement logic for this here
        } else {
            // TODO: Send the action message and the turn message for the next player
        }
    }

    @Override
    public void raise(PokerGame game, GamePlayer player, long chips) {
        checkTurn(game, player);
        long toCall = game.getCurrentBet() - player.getBet();
        long minRaise = game.getMinRaise();

        if (chips < toCall + minRaise) {
            // TODO: Send another turn message for this player
            throw new BadRequestException("Raise must be at least the minimum raise above the current bet");
        }
        if (chips > player.getChips()) {
            // TODO: Send another turn message for this player
            throw new BadRequestException("You do not have enough chips to raise");
        }

        player.setChips(player.getChips() - chips);
        player.setBet(player.getBet() + chips);

        if (game.moveCurrent()) { // The betting round is over, proceed the game
            // TODO: Implement logic for this here
        } else {
            // TODO: Send the action message and the turn message for the next player
        }
    }

    @Override
    public void allIn(PokerGame game, GamePlayer player) {
        checkTurn(game, player);
        long allInAmount = player.getChips(); // For the notification
        player.setBet(player.getBet() + player.getChips());
        player.setChips(0);

        if (game.moveCurrent()) { // The betting round is over, proceed the game
            // TODO: Implement logic for this here
        } else {
            // TODO: Send the action message and the turn message for the next player
        }
    }

    private void checkTurn(PokerGame game, GamePlayer player) {
        if (!game.getCurrent().equals(player)) {
            throw new BadRequestException("It's not your turn");
        }
    }
}