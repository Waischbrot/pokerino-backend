package org.pokerino.backend.domain.cards;

import java.util.concurrent.ThreadLocalRandom;

public final class CardStack {
    private final String[] stack;
    private int cardsDrawn;

    private CardStack(final String[] stack) {
        this.stack = stack;
        this.cardsDrawn = 0;
    }

    /**
     * Find out, how many cards are still on this stack to pull!
     *
     * @return Size of stack to pull cards from
     */
    public int size() {
        return this.stack.length - this.cardsDrawn;
    }

    /**
     * How many cards have we already taken off this stack?
     *
     * @return Amount of cards taken
     */
    public int cardsDrawn() {
        return this.cardsDrawn;
    }

    /**
     * Pull a random card from the stack.
     * The card is removed from the stack and cannot be drawn again.
     *
     * @return A random card from the stack
     */
    public String take() {
        final int size = this.size();
        if (size <= 0) {
            throw new IllegalStateException("No cards left in stack!");
        }
        final int index = ThreadLocalRandom.current().nextInt(0, size);
        final String card = this.stack[index];
        this.stack[index] = this.stack[size - 1];
        this.stack[size - 1] = card;
        this.cardsDrawn++;
        return card;
    }

    private static final char[] VALUES = { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };
    private static final char[] SUITS = { 's', 'c', 'h', 'd' }; // diamond heart spade club

    /**
     * Loops through all possible suits and values to create an array with every card which is in a poker deck.
     *
     * @return A new CardStack instance to pull cards from!
     */
    public static CardStack create() {
        final String[] stack = new String[52];
        int index = 0;
        for (final char suit : SUITS) {
            for (final char value : VALUES) {
                stack[index] = String.valueOf(value) + suit;
                index++;
            }
        }
        return new CardStack(stack);
    }
}
