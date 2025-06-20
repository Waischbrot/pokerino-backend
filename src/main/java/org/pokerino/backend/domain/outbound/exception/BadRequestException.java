package org.pokerino.backend.domain.outbound.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public final class BadRequestException extends OutboundException {
    public BadRequestException(final String message) {
        super(message);
    }

    @Override
    @NonNull
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
