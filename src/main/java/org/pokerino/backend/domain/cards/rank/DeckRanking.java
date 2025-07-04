package org.pokerino.backend.domain.cards.rank;

import lombok.NonNull;

import java.util.Arrays;

public record DeckRanking(int... indexes) {
    /**
     * Play this DeckRanking against another DeckRanking.
     *
     * @param other The DeckRanking to compare against
     * @return 1 if this DeckRanking is better, -1 if this DeckRanking is worse, 0 if both are equal
     */
    public int playAgainst(final DeckRanking other) {
        for (int i = 0; i < indexes.length; i++) {
            if (other.indexes.length < (i + 1)) {
                return 1;
            }
            if (this.indexes[i] > other.indexes[i]) {
                return 1;
            }
            if (this.indexes[i] < other.indexes[i]) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Represent this ranking as String, used for debug purposes!
     *
     * @return String representation
     */
    @Override
    @NonNull
    public String toString() {
        return "DeckRanking{" +
                "indexes=" + Arrays.toString(indexes) +
                '}';
    }
}
