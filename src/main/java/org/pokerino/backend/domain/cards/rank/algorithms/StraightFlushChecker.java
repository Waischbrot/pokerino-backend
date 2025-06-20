package org.pokerino.backend.domain.cards.rank.algorithms;

import java.util.Arrays;

public final class StraightFlushChecker {
    private static final char[] SUITS = { 's', 'c', 'h', 'd' };

    /**
     * Receives a deck of cards and checks if there is a straight flush.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * A straight flush is a combination of a straight and a flush, e.g. "2", "3", "4", "5", "6" of the same suit.
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no straight flush was found, 0 for the weakest straight flush, ascending for every stronger straight flush!
     */
    public static int checkForStraightFlush(final String[] deck) {
        for (final char suit : SUITS) {
            int[] values = new int[8];
            int count = 0;
            boolean hasAce = false;
            for (final String card : deck) {
                if (card.charAt(1) == suit) {
                    int val = cardValue(card.charAt(0));
                    if (val == 14) {
                        hasAce = true;
                    }
                    values[count++] = val;
                }
            }
            if (hasAce) {
                values[count++] = 1;
            }
            Arrays.sort(values, 0, count);
            for (int i = count - 5; i >= 0; i--) {
                if (isConsecutive(values, i, i + 5)) {
                    int highestCard = values[i + 4];
                    int rank;

                    if (highestCard == 5) {
                        rank = 0;
                    } else {
                        rank = highestCard - 5;
                    }
                    return rank;
                }
            }
        }
        return -1;
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

    private static boolean isConsecutive(final int[] values, int start, int end) {
        for (int i = start + 1; i < end; i++) {
            if (values[i] != values[i - 1] + 1) {
                return false;
            }
        }
        return true;
    }
}