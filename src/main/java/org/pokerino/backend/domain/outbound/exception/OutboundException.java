package org.pokerino.backend.domain.outbound.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public abstract class OutboundException extends RuntimeException {
    public OutboundException(String message){
        super(message);
    }

    @NonNull
    public abstract HttpStatus getStatus();

}
