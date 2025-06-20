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

    public GamePlayer(long userId, int total) {
        this.userId = userId;
        this.total = total;
    }

    /**
     * Checks if the player is still in the game but has no more chips left. (ALL-IN)
     * @return True if the player is all-in, false otherwise.
     */
    public boolean isAllIn() {
        if (dead) {
            return false;
        }
        return this.total == 0;
    }

    /**
     * Sets the hand of the player. Only to be called with 2 cards.
     * @param hand The hand of the player.
     */
    public void setHand(String... hand) {
        if (hand.length != 2) {
            throw new IllegalStateException("Too many cards present!");
        }
        this.hand = hand;
    }

    public boolean bet(int numberOfChips){
        if(numberOfChips>total){
            return false;
        }
        total-=numberOfChips;
        bet+=numberOfChips;
        return true;
    }
}
