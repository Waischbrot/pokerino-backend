package org.pokerino.backend.domain.exception.game;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException(Long id) {
        super("Game with id "+id+"isn't found");
    }
}
