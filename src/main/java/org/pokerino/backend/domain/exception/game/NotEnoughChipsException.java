package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class NotEnoughChipsException extends GameException {
    public NotEnoughChipsException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
