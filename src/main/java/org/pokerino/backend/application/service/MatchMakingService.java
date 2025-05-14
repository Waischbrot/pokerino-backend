package org.pokerino.backend.application.service;

import java.util.UUID;

import org.pokerino.backend.application.port.in.MatchMakingUseCase;
import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.domain.exception.game.GameNotFoundException;
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

    @Override
    public boolean addPlayer(UUID gameId, long userId) {
        try {
            var temp = loadGamePort.getGame(gameId);
            if ( temp.isEmpty() )
                return false;
            PokerGame pokerGame = temp.get();
            pokerGame.addPlayer(userId);
            return true;
        } catch (Exception e) { //GlobalHandler? why not RuntimeException? check if all exeception are thrown
            return false;
        }
        
    }

    @Override
    public boolean addPlayer(Table table, long userId) {
        try {
            var temp = loadGamePort.getGamesByTable(table);
            if ( temp.isEmpty() )
                return false;
            PokerGame pokerGame = temp.getFirst(); // At which game take part ??
            pokerGame.addPlayer(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PokerGame getGame(long userId) {
            var temp = loadGamePort.getGame(userId);
            if ( temp.isEmpty() )
                throw new GameNotFoundException(userId); // ControllerAdvice to return 404 Error to client ?
            return temp.get();
    }

    @Override
    public boolean deletePlayer(UUID gameId, long userId) {
        try {
            var temp = loadGamePort.getGame(gameId);
            if ( temp.isEmpty() )
                return false;
            PokerGame pokerGame = temp.get();
            pokerGame.removePlayer(userId);
            return true;
        } catch (Exception e) { //GlobalHandler?
            return false;
        }
    }

    @Override
    public boolean deletePlayer(Table table, long userId) {
        try {
            var temp = loadGamePort.getGamesByTable(table);
            if ( temp.isEmpty() )
                return false;
            PokerGame pokerGame = temp.getFirst(); // At which game take part ??
            pokerGame.removePlayer(userId);
            return true;
        } catch (Exception e) { //GlobalHandler?
            return false;
        }
    }
                

    
}
