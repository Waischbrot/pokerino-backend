package org.pokerino.backend.domain.rank.algorithms;

public final class RoyalFlushChecker {
    private static final char[] SUITS = { 's', 'c', 'h', 'd' };
    private static final char[] ROYAL_FLUSH_CARDS = { 'T', 'J', 'Q', 'K', 'A' };

    /**
     * Receives a deck of cards and checks if there is a royal flush.
     * Input array always contains 7 cards.
     * Example: { "6h", "2h", "3h", "4h", "5h", "9h", "7h" }
     * A royal flush consists of the following cards: "T", "J", "Q", "K", "A" of the same suit.
     *
     * @param deck The deck of cards to evaluate.
     * @return true if found, false otherwise.
     */
    public static boolean checkForRoyalFlush(final String[] deck) {
        for (final char suit: SUITS) {
            boolean success = true;
            for (final char card: ROYAL_FLUSH_CARDS) {
                if (!hasCard(deck, String.valueOf(card) + suit)) {
                    success = false;
                    break;
                }
            }
            if (success) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCard(final String[] deck, final String search) {
        for (final String card : deck) {
            if (card.equals(search)) {
                return true;
            }
        }
        return false;
    }
}
