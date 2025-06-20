package org.pokerino.backend.adapter.in.dto;

public record HostGameRequestDto(
    String tableName,
    int maxPlayers,
    int turnTime,
    int startBalance,
    int smallBlind,
    boolean increasingBlind
)
{}