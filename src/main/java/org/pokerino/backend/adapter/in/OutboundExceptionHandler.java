package org.pokerino.backend.adapter.in;

import org.pokerino.backend.adapter.in.response.ErrorResponse;
import org.pokerino.backend.domain.outbound.exception.OutboundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OutboundExceptionHandler {
    @ExceptionHandler(OutboundException.class)
    public ResponseEntity<ErrorResponse> handleOutboundException(final OutboundException exception) {
        final var message = exception.getMessage();
        final var errorResponse = (message != null) ? new ErrorResponse(message) : new ErrorResponse();
        return ResponseEntity
                .status(exception.getStatus())
                .body(errorResponse);
    }

}
