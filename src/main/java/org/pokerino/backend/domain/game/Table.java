package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum Table {
    ;

    String name; // The name of the table
    int buyIn; // How much does it cost to join the table
    int startingChips; // How many chips does each player start with
    int smallBlind; // The minimum bet
    int bigBlind; // Normally double the small blind
    int blindLevelSeconds; // After how many seconds will the blinds increase (normally doubled)
    int firstPlacePrice; // How much does the first place get?
    int secondPlacePrice; // How much does the second place get?
}