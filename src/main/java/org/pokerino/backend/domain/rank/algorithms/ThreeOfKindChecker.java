package org.pokerino.backend.domain.rank.algorithms;

public final class ThreeOfKindChecker {
    /**
     * Receives a deck of cards and checks if there is a three of a kind.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * Three of a kind is a combination of three cards with the same value, e.g. "2", "2", "2".
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no three of a kind was found, otherwise an array with the value of the three of a kind and the two other highest cards.
     */
    public static int[] checkForThreeOfKind(final String[] deck) {
        int[] counts = new int[15];
        for (int i = 0; i < 7; i++) {
            int v = cardValue(deck[i].charAt(0));
            counts[v]++;
        }
        int threeValue = -1;
        for (int v = 14; v >= 2; v--) {
            if (counts[v] >= 3) {
                threeValue = v;
                counts[v] -= 3;
                break;
            }
        }
        if (threeValue == -1) {
            return new int[]{ -1 };
        }
        int[] otherCards = new int[4];
        int index = 0;
        for (int v = 14; v >= 2 && index < 4; v--) {
            while (counts[v] > 0 && index < 4) {
                otherCards[index++] = v;
                counts[v]--;
            }
        }
        return new int[]{ threeValue - 2, otherCards[0] - 2, otherCards[1] - 2 };
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
