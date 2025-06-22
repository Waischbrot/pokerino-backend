package org.pokerino.backend.adapter.in.dto.game;

public record HostGameDto(
    String name,
    int maxPlayers,
    int turnTime,
    long startBalance,
    long smallBlind,
    boolean increasingBlind
) {}