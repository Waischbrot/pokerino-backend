package org.pokerino.backend.adapter.in.dto;

public record ResetPasswordDto(
        String email,
        String password,
        String token
) {}
