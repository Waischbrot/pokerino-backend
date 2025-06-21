package org.pokerino.backend.adapter.out.persistence;

import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.domain.game.PokerGame;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class GameInMemoryRepository implements LoadGamePort, ManageGamePort {
    private final ConcurrentHashMap<String, PokerGame> games;

    public GameInMemoryRepository() {
        this.games = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<PokerGame> getGame(String gameCode) {
        final PokerGame result = this.games.get(gameCode);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<PokerGame> getUserGame(String username) {
        for (final PokerGame game : this.games.values()) {
            if (game.isParticipant(username)) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveGame(PokerGame pokerGame) {
        this.games.put(pokerGame.getGameCode(), pokerGame);
    }

    @Override
    public void removeGame(String gameCode) {
        this.games.remove(gameCode);
    }

    @Override
    public String generateGameCode() {
        while (true) {
            final String code = ThreadLocalRandom.current()
                    .ints(6, 0, 10)
                    .mapToObj(String::valueOf)
                    .reduce("", String::concat);
            if (!this.games.containsKey(code)) {
                return code;
            }
        }
    }
}