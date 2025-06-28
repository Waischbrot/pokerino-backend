package org.pokerino.backend.application.port.in.game;

import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;

public interface GameUseCase {
    void win(PokerGame game, GamePlayer player);
    void nextTurn(PokerGame game);
    void endBettingRound(PokerGame game);
    void rememberTurn(PokerGame game);
}
