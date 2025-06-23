package org.pokerino.backend.application.port.in.game;

import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;

public interface TurnUseCase {
    void fold(PokerGame game, GamePlayer player);
    void check(PokerGame game, GamePlayer player);
    void call(PokerGame game, GamePlayer player);
    void raise(PokerGame game, GamePlayer player, int numberOfChips);
    void allIn(PokerGame game, GamePlayer player);
}
