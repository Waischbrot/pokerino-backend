package org.pokerino.backend.adapter.in.dto;

public record VerifyUserDto(
    String email,
    String verificationCode
) {}