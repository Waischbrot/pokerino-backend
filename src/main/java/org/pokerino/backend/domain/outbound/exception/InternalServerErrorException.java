package org.pokerino.backend.domain.outbound.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public final class InternalServerErrorException extends OutboundException {
    public InternalServerErrorException(final String message) {
        super(message);
    }

    @Override
    @NonNull
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
