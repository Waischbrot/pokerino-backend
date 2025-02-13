package org.pokerino.backend.application.port.in;

import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;

import java.util.List;

public interface FindStrongestHandUseCase {
    /**
     * Find the strongest hands in the game, used at the end of every round
     * @param game The game to evaluate
     * @return The list of players with the strongest hands
     */
    List<GamePlayer> findStrongestHands(final PokerGame game);
}
