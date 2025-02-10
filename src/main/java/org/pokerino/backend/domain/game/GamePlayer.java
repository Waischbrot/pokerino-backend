package org.pokerino.backend.domain.game;

import lombok.Getter;

@Getter
public class GamePlayer {
    private int total;
    private int bet;
    private String[] hand;
    // Todo: Add fields that are needed for UI here...

    public void setHand(String... hand) {
        if (hand.length != 2) {
            throw new IllegalStateException("Too many cards present!");
        }
        this.hand = hand;
    }
}
