package org.pokerino.backend.adapter.in.response.game;

public record TableOptionsResponse(
        String name,
        int maxPlayers,
        int turnTime,
        long startBalance,
        long smallBlind,
        boolean increasingBlind
) {}
