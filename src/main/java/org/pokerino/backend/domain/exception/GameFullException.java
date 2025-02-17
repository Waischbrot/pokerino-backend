package org.pokerino.backend.domain.exception;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(message);
    }
}
