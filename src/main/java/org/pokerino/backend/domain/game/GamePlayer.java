package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GamePlayer {
    final long userId;
    @Setter int total;
    @Setter int bet;
    String[] hand;
    // Todo: Add fields that are needed for UI here...

    public GamePlayer(long userId, int total) {
        this.userId = userId;
        this.total = total;
    }

    public void setHand(String... hand) {
        if (hand.length != 2) {
            throw new IllegalStateException("Too many cards present!");
        }
        this.hand = hand;
    }
}
