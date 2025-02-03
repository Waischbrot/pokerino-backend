package org.pokerino.backend.domain.rank.algorithms;

public final class FullHouseChecker {
    private static final char[] SUITS = { 's', 'c', 'h', 'd' };
    private static final char[] VALUES = { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };

    /**
     * Receives a deck of cards and checks if there is a full house.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * A full house is a combination of three cards with the same value and two cards with another value, e.g. "2", "2", "2", "3", "3".
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no full house was found, otherwise an array with the values of the three cards and the two cards.
     */
    public static int[] checkForFullHouse(final String[] deck) {
        char threeCards = '0';
        char twoCards = '0';
        for (final char value: VALUES) {
            int count = 0;
            for (final char suit: SUITS) {
                if (hasCard(deck, String.valueOf(value) + suit)) {
                    count++;
                }
            }
            if (count >= 3 && threeCards == '0' ) {
                threeCards = value;
            } else if (count >= 2 && twoCards == '0') {
                twoCards = value;
            }
        }
        if (threeCards != '0' && twoCards != '0') {
            return new int[]{ cardValue(threeCards) - 2, cardValue(twoCards) - 2 };
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