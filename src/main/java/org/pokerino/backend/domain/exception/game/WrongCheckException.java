package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class WrongCheckException extends GameException{
    public WrongCheckException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
