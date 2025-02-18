package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

import java.util.Optional;
import java.util.UUID;

public interface GamesRepository {
    Optional<PokerGame> getGame(UUID gameId);

    Optional<PokerGame> getGame(long userId);

    boolean hasGame(UUID gameId);

    void saveGame(PokerGame pokerGame);

    void removeGame(UUID gameId);

    UUID generateGameId();
}
