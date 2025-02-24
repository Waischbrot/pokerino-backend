package org.pokerino.backend.adapter.in.response;

public record RegisterResponse(
    long id,
    String username,
    String email,
    boolean enabled
) {}
