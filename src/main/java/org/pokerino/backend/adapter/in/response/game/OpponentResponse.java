package org.pokerino.backend.adapter.in.response.game;

public record OpponentResponse(
        String username,
        boolean host, // True if the player is the host of the game
        long chips,
        long bet,
        boolean folded,
        boolean dead
) {}
