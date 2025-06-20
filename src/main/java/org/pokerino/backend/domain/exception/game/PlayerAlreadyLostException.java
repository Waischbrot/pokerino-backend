package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class PlayerAlreadyLostException extends GameException {
    public PlayerAlreadyLostException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
