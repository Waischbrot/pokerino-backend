package org.pokerino.backend.domain.cards.rank.algorithms;

public final class PairChecker {
    /**
     * Receives a deck of cards and checks if there is a pair.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * A Pair is a combination of two cards with the same value, e.g. "2", "2".
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no pair was found, otherwise an array with the value of the pair and the three other highest cards.
     */
    public static int[] checkForPair(final String[] deck) {
        int[] count = new int[15];
        for (int i = 0; i < 7; i++) {
            int v = cardValue(deck[i].charAt(0));
            count[v]++;
        }
        int pairValue = -1;
        for (int v = 14; v >= 2; v--) {
            if (count[v] >= 2) {
                pairValue = v;
                count[v] -= 2;
                break;
            }
        }
        if (pairValue == -1) {
            return new int[]{ -1 };
        } else {
            int[] otherCards = new int[5];
            int index = 0;
            for (int v = 14; v >= 2; v--) {
                while (count[v] > 0) {
                    otherCards[index++] = v;
                    count[v]--;
                }
            }
            return new int[]{ pairValue - 2, otherCards[0] - 2, otherCards[1] - 2, otherCards[2] - 2};
        }
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
