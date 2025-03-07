package org.pokerino.backend.domain.exception.game;

public final class UserAlreadyPresentException extends RuntimeException {
    public UserAlreadyPresentException(String errorMessage) {
        super(errorMessage);
    }
}
