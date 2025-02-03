package org.pokerino.backend.domain.rank.algorithms;

public final class StraightChecker {
    /**
     * Receives a deck of cards and checks if there is a straight.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * ATTENTION: A Straight can begin and end with an Ace (A), e.g. "A", "2", "3", "4", "5" or "10", "J", "Q", "K", "A".
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no straight was found, 0 for the weakest straight, ascending for every stronger straight!
     */
    public static int checkForStraight(final String[] deck) {
        boolean[] valuePresent = new boolean[15];
        for (String card : deck) {
            int val = cardValue(card.charAt(0));
            valuePresent[val] = true;
        }
        if (valuePresent[14]) {
            valuePresent[1] = true;
        }
        int maxRankFound = -1;
        for (int i = 1; i <= 10; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 5; j++) {
                if (!valuePresent[i + j]) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                int highestCard = i + 4;
                int rank;
                if (highestCard == 5) {
                    rank = 0;
                } else if (highestCard == 14) {
                    rank = 9;
                } else {
                    rank = highestCard - 5;
                }
                if (rank > maxRankFound) {
                    maxRankFound = rank;
                }
            }
        }
        return maxRankFound;
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
