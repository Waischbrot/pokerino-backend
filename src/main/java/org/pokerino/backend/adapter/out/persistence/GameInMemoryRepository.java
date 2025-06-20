package org.pokerino.backend.adapter.out.persistence;

import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.domain.game.PokerGame;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameInMemoryRepository implements LoadGamePort, ManageGamePort {
    private final ConcurrentHashMap<UUID, PokerGame> games;

    public GameInMemoryRepository() {
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
            if (game.containsInGame(userId)) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean hasGame(UUID gameId) {
        return this.games.containsKey(gameId);
    }

    // not needed anymore?? because there are no table types know

    // @Override
    // public List<PokerGame> getGamesByTable(Table table) {
    //     final List<PokerGame> games = new ArrayList<>();
    //     for (final PokerGame game : this.games.values()) {
    //         if (game.getTable() == table) {
    //             games.add(game);
    //         }
    //     }
    //     return games;
    // }

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