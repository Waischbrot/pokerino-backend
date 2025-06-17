package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadGamePort {
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
     * Designed to get all games by a specific table
     * @param table Table object
     * @return List of PokerGame objects associated with the table
     */
    List<PokerGame> getGamesByTable(Table table);
}
