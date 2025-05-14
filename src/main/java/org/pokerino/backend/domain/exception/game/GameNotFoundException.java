package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends GameException{
    public GameNotFoundException(Long id) {
        super("Game with id "+id+"isn't found");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
