package org.pokerino.backend.adapter.in.response;

public record ErrorResponse(String error) {
    public ErrorResponse() {
        this("Unknown error");
    }
}