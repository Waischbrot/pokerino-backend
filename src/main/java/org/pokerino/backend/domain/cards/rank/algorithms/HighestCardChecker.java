package org.pokerino.backend.domain.cards.rank.algorithms;
import java.util.Arrays;

public final class HighestCardChecker {
    /**
     * Returns the 5 highest cards of the deck in descending order.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     *
     * @param deck The deck of cards to evaluate.
     * @return An array with the 5 highest card values of the deck in descending order.
     */
    public static int[] checkHighestCard(final String[] deck) {
        int[] values = new int[7];
        for (int i = 0; i < 7; i++) {
            values[i] = cardValue(deck[i].charAt(0));
        }
        Arrays.sort(values);
        int[] highestCards = new int[5];
        for (int i = 0; i < 5; i++) {
            highestCards[i] = values[6 - i] - 2;
        }
        return highestCards;
    }

    private static int cardValue(char card) {
        return switch (card) {
            case 'T' -> 10;
            case 'J' -> 11;
            case 'Q' -> 12;
            case 'K' -> 13;
            case 'A' -> 14;
            default -> card - '0';
        };
    }
}
