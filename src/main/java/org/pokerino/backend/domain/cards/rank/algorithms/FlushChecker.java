package org.pokerino.backend.domain.cards.rank.algorithms;

import java.util.Arrays;

public final class FlushChecker {
    private static final char[] SUITS = { 's', 'c', 'h', 'd' };

    /**
     * Receives a deck of cards and checks if there is a flush.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * A Flush is a combination of five cards of the same suit.
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no flush was found, otherwise an array with the values of the five highest cards of the flush.
     */
    public static int[] checkForFlush(final String[] deck) {
        for (final char suit: SUITS) {
            int[] values = new int[7];
            int count = 0;
            for (final String card : deck) {
                if (card.charAt(1) == suit) {
                    values[count++] = cardValue(card.charAt(0)) - 2;
                }
            }
            if (count >= 5) {
                Arrays.sort(values, 0, count);
                return new int[]{ values[count - 1], values[count - 2], values[count - 3], values[count - 4], values[count - 5] };
            }
        }
        return new int[]{ -1 };
    }

    private static boolean hasCard(final String[] deck, final String search) {
        for (final String card : deck) {
            if (card.equals(search)) {
                return true;
            }
        }
        return false;
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