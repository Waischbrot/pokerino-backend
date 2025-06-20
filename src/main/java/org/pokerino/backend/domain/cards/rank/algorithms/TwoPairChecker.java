package org.pokerino.backend.domain.cards.rank.algorithms;

public final class TwoPairChecker {
    /**
     * Receives a deck of cards and checks if there are two pairs.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * Two Pairs is a combination of two pairs of cards with the same value, e.g. "2", "2", "3", "3".
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no two pairs were found, otherwise an array with the values of the two pairs and the highest other card.
     */
    public static int[] checkForTwoPair(final String[] deck) {
        int[] counts = new int[15];
        for (int i = 0; i < 7; i++) {
            int value = cardValue(deck[i].charAt(0));
            counts[value]++;
        }
        int highPair = -1, lowPair = -1;
        for (int v = 14; v >= 2; v--) {
            if (counts[v] >= 2) {
                if (highPair == -1) {
                    highPair = v;
                } else {
                    lowPair = v;
                    break;
                }
            }
        }
        if (lowPair == -1) {
            return new int[] { -1 };
        }
        counts[highPair] -= 2;
        counts[lowPair] -= 2;
        int highestOtherCard = -1;
        for (int v = 14; v >= 2; v--) {
            if (counts[v] > 0) {
                highestOtherCard = v;
                break;
            }
        }
        return new int[] { highPair, lowPair, highestOtherCard };
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