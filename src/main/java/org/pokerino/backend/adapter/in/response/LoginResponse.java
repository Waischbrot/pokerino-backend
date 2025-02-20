package org.pokerino.backend.adapter.in.response;

public record LoginResponse(
    String token,
    long expiresIn
) {}
