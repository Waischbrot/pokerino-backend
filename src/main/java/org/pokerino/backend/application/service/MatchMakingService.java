package org.pokerino.backend.application.service;

import java.util.List;
import java.util.UUID;

import org.pokerino.backend.application.port.in.MatchMakingUseCase;
import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.domain.exception.game.GameAlreadyStartedException;
import org.pokerino.backend.domain.exception.game.GameFullException;
import org.pokerino.backend.domain.exception.game.GameNotFoundException;
import org.pokerino.backend.domain.exception.game.PlayerNotPresentException;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.game.Table;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import  lombok.AccessLevel;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class MatchMakingService implements MatchMakingUseCase {
    LoadGamePort loadGamePort;
    ManageGamePort manageGamePort;

    @Override
    public void addPlayer(UUID gameId, long userId) {
        var temp = loadGamePort.getGame(gameId);
        if ( temp.isEmpty() ) throw new GameNotFoundException(gameId);
        PokerGame pokerGame = temp.get();
        pokerGame.addPlayer(userId);
    }

    @Override
    public void addPlayer(Table table, long userId) {
        var temp = loadGamePort.getGamesByTable(table); 
        for (PokerGame pokerGame : temp) {
                try{
                        pokerGame.addPlayer(userId);
                        return;
                }
                catch(GameAlreadyStartedException | GameFullException ex){
                        continue;
                }  
        }
        PokerGame game = new PokerGame( manageGamePort.generateGameId(), table);
        manageGamePort.saveGame(game);
        game.addPlayer(userId);
    }

    @Override
    public PokerGame getGame(long userId) {
            var temp = loadGamePort.getGame(userId);
            if ( temp.isEmpty() )
                throw new PlayerNotPresentException("Player with userId"+ userId+ "is present in no game");
            return temp.get();
    }

    @Override
    public void deletePlayerFromQueue(UUID gameId, long userId) {
            var temp = loadGamePort.getGame(gameId);
            if ( temp.isEmpty() ) throw new GameNotFoundException(gameId);
            PokerGame pokerGame = temp.get();
            pokerGame.removePlayerFromQueue(userId);
    }

    @Override
    public void deletePlayerFromQueue(Table table, long userId) {
            var temp = loadGamePort.getGamesByTable(table);
            if ( temp.isEmpty() ) throw new GameNotFoundException(table.getName());
            for (PokerGame pokerGame : temp) {
                if (pokerGame.containsInGame(userId)){
                        pokerGame.removePlayerFromQueue(userId);
                        return;
                }
            }
            throw new PlayerNotPresentException("Player with userId"+userId+"is present in no queue of games of this table");

    }

    @Override
    public boolean isInQueue(UUID gameId, long userId) {
        var temp = loadGamePort.getGame(gameId);
        if ( temp.isEmpty() ) throw new GameNotFoundException(gameId);
        if( temp.get().containsInQueue(userId)) return true;
        return false;
    }

    @Override
    public List<GamePlayer> getWaitingPlayers(UUID gameId) {
        return loadGamePort.getGame(gameId).get().getUsersInQueue();
    }

    @Override
    public int getQueueSize(UUID gameId) {
        return loadGamePort.getGame(gameId).get().getUsersInQueue().size();
    }
                

    
}
