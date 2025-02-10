package org.pokerino.backend.domain.exception;

public final class UserNotPresentException extends RuntimeException {
    public UserNotPresentException(String errorMessage) {
        super(errorMessage);
    }
}
