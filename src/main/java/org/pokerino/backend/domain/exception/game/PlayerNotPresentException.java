package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public final class PlayerNotPresentException extends GameException {
    public PlayerNotPresentException(String errorMessage) {
        super(errorMessage);
    }
    public HttpStatus getStatus(){
        return HttpStatus.CONFLICT;
    }
}
