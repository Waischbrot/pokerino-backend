package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class WrongRaiseException extends GameException {
    public WrongRaiseException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}