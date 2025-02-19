package org.pokerino.backend.adapter.in.dto;

public record RegisterUserDto(
        String email,
        String password,
        String username
) {}
