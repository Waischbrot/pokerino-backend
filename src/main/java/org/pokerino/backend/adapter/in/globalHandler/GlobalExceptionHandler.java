package org.pokerino.backend.adapter.in.globalHandler;

import org.pokerino.backend.adapter.in.response.ErrorResponse;
import org.pokerino.backend.domain.exception.game.GameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponse> handleGameExecption(GameException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponse(ex.getMessage()));
    }

}
