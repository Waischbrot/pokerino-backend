package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class CallIsNotNeededException extends GameException {
    public CallIsNotNeededException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}