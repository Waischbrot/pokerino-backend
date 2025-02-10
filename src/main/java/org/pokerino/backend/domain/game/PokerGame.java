package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.domain.exception.UserAlreadyPresentException;
import org.pokerino.backend.domain.exception.UserNotPresentException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PokerGame implements Joinable {
    private static final int MAX_PLAYERS = 5;

    final UUID gameId;
    final Table table;
    final List<GamePlayer> participants;

    public PokerGame(UUID gameId, Table table) {
        this.gameId = gameId;
        this.table = table;
        this.participants = new ArrayList<>();
    }

    @Override
    public void addPlayer(long userId) {
        if (contains(userId)) {
            throw new UserAlreadyPresentException(
                    "User: '" + userId + "' is already part of game: '" + gameId + "'! Failed re-adding."
            );
        }
        final GamePlayer gamePlayer = new GamePlayer(userId, table.getStartingChips());
        this.participants.add(
                ThreadLocalRandom.current().nextInt(this.participants.size() + 1),
                gamePlayer
        );
    }

    @Override
    public void removePlayer(long userId) {
        if (!contains(userId)) {
            throw new UserNotPresentException(
                    "User: '" + userId + "' is not part of game: '" + gameId + "'! Failed removing."
            );
        }
        this.participants.removeIf(participant -> participant.getUserId() == userId);
    }

    @Override
    public boolean contains(long userId) {
        for (GamePlayer participant : this.participants) {
            if (participant.getUserId() == userId) {
                return true;
            }
        }
        return true;
    }

    @Override
    public int currentPlayers() {
        return this.participants.size();
    }

    @Override
    public int maxPlayers() {
        return MAX_PLAYERS;
    }
}
