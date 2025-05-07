package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum Table {
    // Task: Think of 7 different tables with unique buy ins and values for players to play at!
    LowStakes("LowStakes",10000,1000,10,20,360,30000,0),
    BigStakes("BigStakes",100000,20000,1000,2000,360,300000,0),
    LowStakesBlitz("LowStakesBlitz",10000,1000,10,20,180,30000,0),
    BigStakesBlitz("BigStakesBlitz",1000000,20000,1000,2000,360,3000000,0),
    LowStakesTwoWinners("LowStakesTwoWinners",10000,1000,10,20,360,20000,10000),
    BigStakesTwoWinners("BigStakesTwoWinners",1000000,20000,1000,2000,360,200000,100000)
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