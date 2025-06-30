package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.pokerino.backend.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PokerGame {
    final String gameCode;
    final TableOptions options;
    @Setter GameState state;
    long totalChips; // All chips involved -> Used to pay out winnings at the end of the game
    final List<GamePlayer> participants; // All participants including those that lost
    int dealer; // Keeps the index of where the dealer is located
    final String[] cardsOnTable; // Cards in the middle, array values are reset to null after each round
    int current; // The index of the current player, used to determine whose turn it is
    long minRaise; // The minimum raise that can be made by the players

    public PokerGame(String gameCode, TableOptions options) {
        this.gameCode = gameCode;
        this.options = options;
        this.state = GameState.WAITING_FOR_PLAYERS;
        this.participants = new ArrayList<>();
        this.dealer = 0;
        this.cardsOnTable = new String[5]; // Initialise array filled with null
        this.minRaise = options.getSmallBlind() * 2; // The minimum raise is the big blind by default
    }

    // Adds a player to the game, but at this point his chips were already deducted
    public void addPlayer(User user) {
        this.totalChips += options.getStartBalance(); // Add the chips to the total pool
        final GamePlayer gamePlayer = new GamePlayer(user.getUsername(), options.getStartBalance());
        this.participants.add(gamePlayer);
    }

    public void removePlayer(String username) {
        for (final GamePlayer participant : participants) {
            if (participant.getUsername().equals(username)) {
                if (state == GameState.WAITING_FOR_PLAYERS) {
                    this.totalChips -= options.getStartBalance(); // Remove the chips from the total pool
                }
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

    @NonNull
    public GamePlayer getPlayer(String username) {
        return participants.stream()
                .filter(participant -> participant.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Player is not part of game: '" + gameCode + "'."));
    }

    public int playerCount() {
        return this.participants.size();
    }

    public int aliveCount() {
        int count = 0;
        for (GamePlayer participant : this.participants) {
            if (!participant.isDead()) {
                count++;
            }
        }
        return count;
    }

    public long getCurrentBet() {
        long highestBet = 0;
        for (GamePlayer participant : this.participants) {
            if (participant.getBet() > highestBet) {
                highestBet = participant.getBet();
            }
        }
        return highestBet;
    }

    @NonNull
    public GamePlayer getCurrent() {
        return this.participants.get(this.current);
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

    // Returns if the betting round is over
    public boolean moveCurrent() {
        this.current = (this.current + 1) % playerCount();
        if (this.current == this.dealer) {
            // If we are back at the dealer, the betting round is over if not all active players have the same bet
            for (GamePlayer participant : this.participants) {
                if (!participant.isDead() && !participant.isFolded()) {
                    if (participant.getBet() != getCurrentBet()) {
                        return false; // Not all active players have the same bet, continue betting
                    }
                }
            }
            return true;
        }
        return false; // Continue to the next player
    }

    public void resetCards() {
        for (int i = 0; i < 5; i++) {
            this.cardsOnTable[i] = null;
        }
    }

    public void resetParticipants() {
        this.current = this.dealer; // Reset the current player to the dealer and one further
        for (GamePlayer participant : this.participants) {
            participant.setBet(0);
            participant.setFolded(false);
            participant.setHand(null, null);
        }
    }
}