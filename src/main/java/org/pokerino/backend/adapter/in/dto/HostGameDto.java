package org.pokerino.backend.adapter.in.dto;

public record HostGameDto(
    String tableName,
    int maxPlayers,
    int turnTime,
    long startBalance,
    long smallBlind,
    boolean increasingBlind
) {}