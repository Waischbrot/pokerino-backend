package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.domain.exception.game.GameAlreadyStartedException;
import org.pokerino.backend.domain.exception.game.GameFullException;
import org.pokerino.backend.domain.exception.game.PlayerAlreadyPresentException;
import org.pokerino.backend.domain.exception.game.PlayerNotPresentException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PokerGame implements Joinable {
    private static final int MAX_PLAYERS = 5;
    final String gameCode;
    final Privacy privacy;






    final TableSpecification table;
    final List<GamePlayer> participants; // All participants including those that lost
    final List<GamePlayer> players; // Players still actively playing the game
    final List <GamePlayer> usersInQueue; // people in queue
    final String[] cardsOnTable; // Cards in the middle, array values are reset to null after each round
    int dealer; // Keeps the index of where the dealer is located
    boolean started; // Is this game still queueing or has it already begun?

    public PokerGame(UUID uuid, Table table) {
        this.gameId = uuid;
        this.table = table;
        this.participants = new ArrayList<>();
        this.players = new ArrayList<>();
        this.usersInQueue = new ArrayList<>();
        this.cardsOnTable = new String[5]; // Initialise array filled with null
        this.dealer = ThreadLocalRandom.current().nextInt(MAX_PLAYERS); // Pick a random dealer
    }

    @Override
    public void addPlayer(long userId) {
        if (started) {
            throw new GameAlreadyStartedException("Game: '" + gameId + "' has already started! Failed adding user: '" + userId + "'.");
        }
        if (containsInGame(userId)) {
            throw new PlayerAlreadyPresentException("User: '" + userId + "' is already part of game: '" + gameId + "'! Failed re-adding.");
        }
        if (usersInQueue.size() >= MAX_PLAYERS) {
            throw new GameFullException("Game: '" + gameId + "' is full! Failed adding user: '" + userId + "'.");
        }
        final GamePlayer gamePlayer = new GamePlayer(userId, table.getStartingChips());
        this.usersInQueue.add(gamePlayer);
    }

    @Override
    public void removePlayer(long userId) {
        if (!containsInGame(userId)) {
            throw new PlayerNotPresentException("User: '" + userId + "' is not part of game: '" + gameId + "'! Failed removing.");
        }
        this.participants.removeIf(participant -> participant.getUserId() == userId);
    }

    @Override
    public void removePlayerFromQueue(long userId) {
        if (!containsInQueue(userId)) {
            throw new PlayerNotPresentException("User: '" + userId + "' is not part of queue of game: '" + gameId + "'! Failed removing.");
        }
        this.usersInQueue.removeIf(userInQueue -> userInQueue.getUserId() == userId);
    }
    

    @Override
    public boolean containsInGame(long userId) {
        for (GamePlayer participant : this.participants) {
            if (participant.getUserId() == userId) {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean containsInQueue(long userId){
        for (GamePlayer userInQueue : this.usersInQueue) {
            if(userInQueue.getUserId() == userId){
                return true;
            }
        }
        return false;
    }

    @Override
    public int currentPlayers() {
        return this.participants.size();
    }

    @Override
    public int maxPlayers() {
        return MAX_PLAYERS;
    }


    @NonNull
    public GamePlayer getDealer() {
        return this.players.get(this.dealer);
    }

    @NonNull
    public GamePlayer getSmallBlind() {
        return this.players.get((this.dealer + 1) % this.players.size());
    }

    @NonNull
    public GamePlayer getBigBlind() {
        return this.players.get((this.dealer + 2) % this.players.size());
    }

    @NonNull
    public String[] mergeHands(final String[] playerHand) {
        final String[] merged = new String[7];
        System.arraycopy(playerHand, 0, merged, 0, 2);
        System.arraycopy(this.cardsOnTable, 0, merged, 2, 5);
        return merged;
    }

    public void moveDealer() {
        this.dealer = (this.dealer + 1) % this.players.size();
    }

    public void resetCards() {
        for (int i = 0; i < 5; i++) {
            this.cardsOnTable[i] = null;
        }
    }

    public void resetParticipants() {
        for (GamePlayer participant : this.participants) {
            participant.setBet(0);
            participant.setFolded(false);
        }
    }

}