package org.pokerino.backend.domain.exception.game;

import java.util.UUID;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends GameException{
    public GameNotFoundException(UUID gameId) {
        super("Game with id "+gameId+"isn't found");
    }
    public GameNotFoundException(String tableName){
        super("Game with this table" + tableName + "isn't found");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
