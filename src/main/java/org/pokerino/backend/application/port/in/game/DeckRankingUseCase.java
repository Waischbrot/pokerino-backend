package org.pokerino.backend.application.port.in.game;

import org.pokerino.backend.domain.cards.rank.DeckRanking;

public interface DeckRankingUseCase {
    /**
     * Evaluate a hand and get the DeckRanking (a definite rating of the hand)
     *
     * @param hand The hand of cards to evaluate
     * @return The DeckRanking of the hand
     */
    DeckRanking evaluateHand(String[] hand);
}
