package org.pokerino.backend.domain.exception.game;

public final class PlayerNotPresentException extends RuntimeException {
    public PlayerNotPresentException(String errorMessage) {
        super(errorMessage);
    }
}
