package org.pokerino.backend.adapter.out.persistence;

import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.game.Privacy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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
    public Optional<PokerGame> getGame(long userId) {
        for (final PokerGame game : this.games.values()) {
            if (game.containsInGame(userId)) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<PokerGame> getPublicQueues() {
        final List<PokerGame> publicQueues = new ArrayList<>();
        for (final PokerGame game : this.games.values()) {
            if (game.getPrivacy() == Privacy.PUBLIC) {
                publicQueues.add(game);
            }
        }
        return publicQueues;
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
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int CODE_LENGTH = 6;

        while (true) {
            StringBuilder generated = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                generated.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
            }
            if (!this.games.containsKey(generated.toString())) {
                return generated.toString();
            }
        }
    }
}