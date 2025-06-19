package org.pokerino.backend.application.service;

import org.pokerino.backend.adapter.out.persistence.GameInMemoryRepository;
import org.pokerino.backend.application.port.in.GameMovesUseCase;
import org.pokerino.backend.domain.exception.game.CallIsNotNeededException;
import org.pokerino.backend.domain.exception.game.NotEnoughChipsException;
import org.pokerino.backend.domain.exception.game.WrongRaiseException;
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
        int maxBetOnTable = game.getCurrentMaxBet();
        
        if (maxBetOnTable!=player.getBet()){

            if(!player.bet(maxBetOnTable-player.getBet())){

                this.allIn(playerId);

            }

        }
        else throw new CallIsNotNeededException("Player" + playerId+ " has nothing to call");
    }

    @Override
    public void raise(long playerId, int numberOfChips) {
        PokerGame game = gameInMemoryRepository.getGame(playerId).get();
        GamePlayer player = game.getPlayer(playerId);
        int maxBetOnTable = game.getCurrentMaxBet();
        if(numberOfChips>= maxBetOnTable+game.getLastRaise()){

            if(player.bet(numberOfChips)){

                game.setLastRaise(numberOfChips);

            }
            else throw new NotEnoughChipsException("Player" + playerId + " has not enough chips to raise by " + numberOfChips + " chips");
        }
        else throw new WrongRaiseException("The raise of player" + playerId + "with " + numberOfChips + "isn't enough. It should be at least call + double of last raise." );
    }

    @Override
    public void allIn(long playerId) {
        PokerGame game = gameInMemoryRepository.getGame(playerId).get();
        GamePlayer player = game.getPlayer(playerId);
        player.bet(player.getTotal());
    }

    @Override
    public void check(long playerId) {
        PokerGame game = gameInMemoryRepository.getGame(playerId).get();
        GamePlayer player = game.getPlayer(playerId);
        int maxBetOnTable = game.getCurrentMaxBet();
        if(maxBetOnTable>player.getBet()){
            throw new WrongRaiseException("Player " + playerId + " can't check if someone has raised");
        }

    }
    
}
