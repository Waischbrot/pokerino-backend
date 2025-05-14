package org.pokerino.backend.domain.exception.game;

public class GameAlreadyStartedException extends GameExceptions {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
