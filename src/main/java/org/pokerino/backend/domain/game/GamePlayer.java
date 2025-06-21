package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GamePlayer {
    final String username;
    long chips; // How many chips does this player have left
    long bet; // How much of his chips are currently in the pot
    String[] hand; // The hand of the player, should only contain 2 cards.
    boolean folded; // Whenever a player folds, this is set to true.
    boolean dead; // Has this player already lost?

    public GamePlayer(String username, long chips) {
        this.username = username;
        this.chips = chips;
    }

    public boolean isAllIn() {
        if (dead) {
            return false;
        }
        return this.chips == 0;
    }

    public void setHand(String... hand) {
        if (hand.length != 2) {
            throw new IllegalStateException("Too many cards present!");
        }
        this.hand = hand;
    }
}
