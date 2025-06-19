package org.pokerino.backend.application.service;

import org.pokerino.backend.adapter.out.persistence.GameInMemoryRepository;
import org.pokerino.backend.application.port.in.GameMovesUseCase;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;

public class GameMovesService implements GameMovesUseCase {

    GameInMemoryRepository gameInMemoryRepository;

    @Override
    public void fold(long playerId) {
        PokerGame game = gameInMemoryRepository.getGame(playerId).get();
        game.removePlayerFromRound(playerId);
    }

    @Override
    public void call(long playerId) {
        PokerGame game = gameInMemoryRepository.getGame(playerId).get();
        GamePlayer player = game.getPlayer(playerId);
        int betToCall=player.getBet();
        for (GamePlayer otherPlayer : game.getPlayers()) {
            if(otherPlayer.getBet()>betToCall) betToCall = otherPlayer.getBet(); 
        }
        if (betToCall!=player.getBet()){

            if(!player.bet(betToCall-player.getBet())){

                this.allIn(playerId);

            }
            
        }
    }

    @Override
    public void raise(long playerId, int numberOfChips) {
        PokerGame game = gameInMemoryRepository.getGame(playerId).get();
        GamePlayer player = game.getPlayer(playerId);
        if(player.g)
    }

    @Override
    public void allIn(long playerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'allIn'");
    }

    @Override
    public void check(long playerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }
    
}
