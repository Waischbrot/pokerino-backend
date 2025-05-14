package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class PlayerAlreadyPresentException extends GameException {
    public PlayerAlreadyPresentException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
