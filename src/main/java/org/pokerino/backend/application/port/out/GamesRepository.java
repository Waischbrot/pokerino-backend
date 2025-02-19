package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

import java.util.Optional;
import java.util.UUID;

public interface GamesRepository {
    /**
     * Designed to get a specific game by its ID
     * @param gameId UUID of the game
     * @return Optional containing the game if it exists, empty otherwise
     */
    Optional<PokerGame> getGame(UUID gameId);

    /**
     * Designed to check if a specific user is currently in a game
     * @param userId ID of the user
     * @return Optional containing the game if the user is in one, empty otherwise
     */
    Optional<PokerGame> getGame(long userId);

    /**
     * Designed to check if a game with a specific ID exists
     * @param gameId UUID of the game
     * @return True if the game exists, false otherwise
     */
    boolean hasGame(UUID gameId);

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
