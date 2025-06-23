package org.pokerino.backend.application.service.game;

import org.pokerino.backend.application.port.in.game.DeckRankingUseCase;
import org.pokerino.backend.domain.cards.rank.DeckRanking;
import org.pokerino.backend.domain.cards.rank.algorithms.*;
import org.springframework.stereotype.Service;

@Service
public class DeckRankingService implements DeckRankingUseCase {
    @Override
    public DeckRanking evaluateHand(final String[] deck) {
        if (RoyalFlushChecker.checkForRoyalFlush(deck)) {
            return new DeckRanking(9);
        }
        final int straightFlush = StraightFlushChecker.checkForStraightFlush(deck);
        if (straightFlush >= 0) {
            return new DeckRanking(8, straightFlush); // highest straight flush
        }
        final int[] fourOfAKind = FourOfAKindChecker.checkForFourOfAKind(deck);
        if (fourOfAKind[0] >= 0) {
            return new DeckRanking(7, fourOfAKind[0], fourOfAKind[1]); // highest four, highest other card
        }
        final int[] fullHouse = FullHouseChecker.checkForFullHouse(deck);
        if (fullHouse[0] >= 0) {
            return new DeckRanking(6, fullHouse[0], fullHouse[1]); // highest thirds, highest couple
        }
        final int[] flush = FlushChecker.checkForFlush(deck);
        if (flush[0] >= 0) {
            return new DeckRanking(5, flush[0], flush[1], flush[2], flush[3], flush[4]); // highest card, second highest, ...
        }
        final int straight = StraightChecker.checkForStraight(deck);
        if (straight >= 0) {
            return new DeckRanking(4, straight); // counts using the highest card of the straight (careful: A is an exception so must look for value)
        }
        final int[] threeOfAKind = ThreeOfKindChecker.checkForThreeOfKind(deck);
        if (threeOfAKind[0] >= 0) {
            return new DeckRanking(3, threeOfAKind[0], threeOfAKind[1], threeOfAKind[2]); // highest three, highest other card, second highest other card
        }
        final int[] twoPair = TwoPairChecker.checkForTwoPair(deck);
        if (twoPair[0] >= 0) {
            return new DeckRanking(2, twoPair[0], twoPair[1], twoPair[2]); // highest pair, second highest pair, highest other card
        }
        final int[] pair = PairChecker.checkForPair(deck);
        if (pair[0] >= 0) {
            return new DeckRanking(1, pair[0], pair[1], pair[2], pair[3]); // highest pair, other cards ranked...
        }
        final int[] highestCard = HighestCardChecker.checkHighestCard(deck);
        return new DeckRanking(0, highestCard[0], highestCard[1], highestCard[2], highestCard[3], highestCard[4]); // all cards in the players hand sorted by value
    }
}
