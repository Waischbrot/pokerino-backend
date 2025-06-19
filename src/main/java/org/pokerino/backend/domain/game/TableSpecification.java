package org.pokerino.backend.domain.game;

import lombok.Getter;
import lombok.Setter;

import org.pokerino.backend.adapter.in.dto.HostGameRequestDto;
import org.pokerino.backend.domain.exception.game.SmallBlindTooHighException;

import jakarta.annotation.Generated;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableSpecification {

    @Getter 
    String tableName;

    @Getter 
    int maxPlayers;

    @Getter
    int turnTime;

    @Getter 
    int startBalance;

    @NonFinal
    @Setter @Getter 
    int smallBlind;

    @Getter 
    boolean increasingBlind;

    @Getter
    static int gameRoundToRaiseSB = 5;

    public TableSpecification(HostGameRequestDto gameOptions){
        tableName = gameOptions.tableName();
        maxPlayers = gameOptions.maxPlayers();
        turnTime = gameOptions.turnTime();
        startBalance = gameOptions.startBalance();
        increasingBlind = gameOptions.increasingBlind();
        if(checkSmallBlind(gameOptions.smallBlind(),gameOptions.startBalance())){
            smallBlind = gameOptions.smallBlind();
        }
        else throw new SmallBlindTooHighException("Setted small blind is to high");
    }

    private boolean checkSmallBlind(int smallBlind, int startBalance){
        return smallBlind*10 > startBalance;
    }

}
