package org.pokerino.backend.adapter.in.dto;

public record HostGameDto(
    String name,
    int maxPlayers,
    int turnTime,
    long startBalance,
    long smallBlind,
    boolean increasingBlind
) {}