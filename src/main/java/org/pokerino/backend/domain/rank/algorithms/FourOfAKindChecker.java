package org.pokerino.backend.domain.rank.algorithms;

public final class FourOfAKindChecker {
    private static final char[] VALUES = { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };
    private static final char[] SUITS = { 's', 'c', 'h', 'd' };

    /**
     * Receives a deck of cards and checks if there are four of a kind.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * Four of a kind is a combination of four cards with the same value, e.g. "2", "2", "2", "2".
     *
     * @param deck The deck of cards to evaluate.
     * @return -1 if no four of a kind was found, otherwise an array with the value of the four of a kind and the highest other card.
     */
    public static int[] checkForFourOfAKind(final String[] deck) {
        for (final char value: VALUES) {
            boolean success = true;
            for (final char suit: SUITS) {
                if (!hasCard(deck, String.valueOf(value) + suit)) {
                    success = false;
                    break;
                }
            }
            if (success) {
                int fourValue = cardValue(value);
                int highest = 0;
                for (final String card : deck) {
                    int cardValue = cardValue(card.charAt(0));
                    if (cardValue != fourValue && cardValue > highest) {
                        highest = cardValue;
                    }
                }
                return new int[]{ fourValue - 2, highest - 2 };
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