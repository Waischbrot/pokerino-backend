package org.pokerino.backend.adapter.in.response;

import java.util.UUID;

public record RegisterResponse(
    UUID id,
    String username,
    String email,
    boolean enabled
) {}
