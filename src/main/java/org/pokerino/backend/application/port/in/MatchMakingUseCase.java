package org.pokerino.backend.application.port.in;

import java.util.UUID;

import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.game.Table;

public interface MatchMakingUseCase {
    boolean addPlayer(UUID gameId, long userId);
    boolean addPlayer(Table table, long userId);
    boolean deletePlayer(UUID gameId, long userId);
    boolean deletePlayer(Table table, long userId);

    PokerGame getGame(long userId);
    
}
