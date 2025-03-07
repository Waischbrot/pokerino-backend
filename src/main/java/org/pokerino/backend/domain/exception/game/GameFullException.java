package org.pokerino.backend.domain.exception.game;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(message);
    }
}
