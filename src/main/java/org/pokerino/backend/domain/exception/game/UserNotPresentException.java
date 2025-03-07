package org.pokerino.backend.domain.exception.game;

public final class UserNotPresentException extends RuntimeException {
    public UserNotPresentException(String errorMessage) {
        super(errorMessage);
    }
}
