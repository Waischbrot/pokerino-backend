package org.pokerino.backend.domain.exception.game;

import org.springframework.http.HttpStatus;

public abstract class GameException extends RuntimeException {
    
    public GameException(String message){
        super(message);
    }

    public abstract HttpStatus getStatus();

}
