package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.pokerino.backend.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PokerGame {
    final String gameCode;
    final TableOptions options;
    GameState state;
    final List<GamePlayer> participants; // All participants including those that lost
    int dealer; // Keeps the index of where the dealer is located
    final String[] cardsOnTable; // Cards in the middle, array values are reset to null after each round

    public PokerGame(String gameCode, TableOptions options) {
        this.gameCode = gameCode;
        this.options = options;
        this.state = GameState.WAITING_FOR_PLAYERS;
        this.participants = new ArrayList<>();
        this.dealer = ThreadLocalRandom.current().nextInt(options.getMaxPlayers()); // Pick a random dealer
        this.cardsOnTable = new String[5]; // Initialise array filled with null
    }

    // Adds a player to the game, but at this point his chips were already deducted
    public void addPlayer(User user) {
        if (state != GameState.WAITING_FOR_PLAYERS) {
            throw new BadRequestException("Game: '" + gameCode + "' is not in a state to add players!");
        }
        if (playerCount() >= options.getMaxPlayers()) {
            throw new BadRequestException("Game: '" + gameCode + "' is full!");
        }
        if (isParticipant(user.getUsername())) {
            throw new BadRequestException("Player is already part of game: '" + gameCode + "'.");
        }
        final GamePlayer gamePlayer = new GamePlayer(user.getUsername(), options.getStartBalance());
        this.participants.add(gamePlayer);
    }

    public void removePlayer(User user) {
        for (final GamePlayer participant : participants) {
            if (participant.getUsername().equals(user.getUsername())) {
                this.participants.remove(participant);
                return;
            }
        }
        throw new BadRequestException("Player is not part of game: '" + gameCode + "'.");
    }

    public boolean isParticipant(String username) {
        return participants.stream()
                .anyMatch(participant ->
                        participant.getUsername().equals(username)
                );
    }

    public int playerCount() {
        return this.participants.size();
    }

    @NonNull
    public GamePlayer getDealer() {
        return this.participants.get(this.dealer);
    }

    @NonNull
    public GamePlayer getSmallBlind() {
        return this.participants.get((this.dealer + 1) % playerCount());
    }

    @NonNull
    public GamePlayer getBigBlind() {
        return this.participants.get((this.dealer + 2) % playerCount());
    }

    @NonNull
    public String[] mergeHands(final String[] playerHand) {
        final String[] merged = new String[7];
        System.arraycopy(playerHand, 0, merged, 0, 2);
        System.arraycopy(this.cardsOnTable, 0, merged, 2, 5);
        return merged;
    }

    public void moveDealer() {
        this.dealer = (this.dealer + 1) % playerCount();
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
            participant.setHand(null, null);
        }
    }
}