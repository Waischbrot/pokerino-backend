package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public class SmallBlindTooHighException extends GameException{
    public SmallBlindTooHighException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
