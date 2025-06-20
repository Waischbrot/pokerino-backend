package org.pokerino.backend.domain.outbound.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public final class ConflictException extends OutboundException {
    public ConflictException(final String message) {
        super(message);
    }

    @Override
    @NonNull
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
