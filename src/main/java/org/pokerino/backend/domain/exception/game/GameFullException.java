package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public class GameFullException extends GameException {
    public GameFullException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
