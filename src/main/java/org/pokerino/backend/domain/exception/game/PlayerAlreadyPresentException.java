package org.pokerino.backend.domain.exception.game;

public final class PlayerAlreadyPresentException extends RuntimeException {
    public PlayerAlreadyPresentException(String errorMessage) {
        super(errorMessage);
    }
}
