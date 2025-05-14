package org.pokerino.backend.adapter.in.globalHandler;

import org.pokerino.backend.adapter.in.dto.ErrorResponseDto;
import org.pokerino.backend.domain.exception.game.GameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponseDto> handleGameNotFound(GameException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponseDto(ex.getMessage()));
    }

}
