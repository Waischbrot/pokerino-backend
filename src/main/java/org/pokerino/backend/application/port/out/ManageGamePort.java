package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

import java.util.UUID;

public interface ManageGamePort {
    /**
     * Saves this game instance to the repository.
     * It is not necessary to repeat this action after every change, just once to store the new object
     * @param pokerGame The game to be saved
     */
    void saveGame(PokerGame pokerGame);

    /**
     * Removes a game instance from the repository.
     * @param gameId UUID of the game
     */
    void removeGame(UUID gameId);

    /**
     * Generates a new unique game ID. Works by generating a random UUID and checking if it is already in use.
     * @return A new unique game ID
     */
    UUID generateGameId();
}
