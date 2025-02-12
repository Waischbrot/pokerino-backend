package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GamePlayer {
    final long userId;
    @Setter int total; // How many chips does this player have left
    @Setter int bet; // How much of his chips are currently in the pot
    String[] hand;
    @Setter boolean folded; // Whenever a player folds, this is set to true.
    @Setter boolean dead; // Has this player already lost?
    // Todo: Add fields that are needed for UI here...

    public GamePlayer(long userId, int total) {
        this.userId = userId;
        this.total = total;
    }

    public boolean isAllIn() {
        return this.total == 0;
    }

    public void setHand(String... hand) {
        if (hand.length != 2) {
            throw new IllegalStateException("Too many cards present!");
        }
        this.hand = hand;
    }
}
