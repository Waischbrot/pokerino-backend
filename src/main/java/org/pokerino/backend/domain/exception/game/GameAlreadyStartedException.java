package org.pokerino.backend.domain.exception.game;

public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
