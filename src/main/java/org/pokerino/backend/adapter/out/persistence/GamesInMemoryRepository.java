package org.pokerino.backend.adapter.out.persistence;

import org.pokerino.backend.application.port.out.GamesRepository;
import org.pokerino.backend.domain.game.PokerGame;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GamesInMemoryRepository implements GamesRepository {
    private final ConcurrentHashMap<UUID, PokerGame> games;

    public GamesInMemoryRepository() {
        this.games = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<PokerGame> getGame(UUID gameId) {
        final PokerGame result = this.games.get(gameId);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<PokerGame> getGame(long userId) {
        for (final PokerGame game : this.games.values()) {
            if (game.contains(userId)) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean hasGame(UUID gameId) {
        return this.games.containsKey(gameId);
    }

    @Override
    public void saveGame(PokerGame pokerGame) {
        this.games.put(pokerGame.getGameId(), pokerGame);
    }

    @Override
    public void removeGame(UUID gameId) {
        this.games.remove(gameId);
    }

    @Override
    public UUID generateGameId() {
        while (true) {
            final UUID generated = UUID.randomUUID();
            if (!hasGame(generated)) {
                return generated;
            }
        }
    }
}