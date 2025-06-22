package org.pokerino.backend.adapter.in.response.game;

import java.util.List;

public record PlayerResponse(
        String username,
        boolean host, // True if the player is the host of the game
        List<String> cards, // Null if not dealt yet
        long chips,
        long bet,
        boolean folded,
        boolean dead
) {}
