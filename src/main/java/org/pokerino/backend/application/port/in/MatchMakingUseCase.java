package org.pokerino.backend.application.port.in;

import java.util.List;
import java.util.UUID;

import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;

public interface MatchMakingUseCase {
    void addPlayer(UUID gameId, long userId);
    //void addPlayer(Table table, long userId); // not needed anymore??
    void deletePlayerFromQueue(UUID gameId, long userId);
    //void deletePlayerFromQueue(Table table, long userId); // same
    void deletePlayerFromGame(UUID gameId, long userId);
    boolean isInQueue(UUID gameId, long userId);


    PokerGame getGame(long userId);
    List<GamePlayer> getWaitingPlayers(UUID gameId);
    int getQueueSize(UUID gameid);
    
    
}
