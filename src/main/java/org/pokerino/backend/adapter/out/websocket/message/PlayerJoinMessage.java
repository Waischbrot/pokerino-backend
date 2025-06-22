package org.pokerino.backend.adapter.out.websocket.message;

public record PlayerJoinMessage(
        String username,
        int playerCount
) {}
