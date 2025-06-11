package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public class GameAlreadyStartedException extends GameException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
