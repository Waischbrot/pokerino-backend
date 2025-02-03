package org.pokerino.backend.domain.rank.algorithms;

public final class RoyalFlushChecker {
    private static final char[] SUITS = { 's', 'c', 'h', 'd' };
    private static final char[] ROYAL_FLUSH_CARDS = { 'T', 'J', 'Q', 'K', 'A' };

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
