package org.pokerino.backend.domain.exception.auth;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
