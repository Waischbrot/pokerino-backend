package org.pokerino.backend.domain.exception.auth;

/**
 * Exception thrown when auth registration endpoint is called and there
 * is already a user with email or username, since they have to be unique!
 */
public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
