package org.pokerino.backend.adapter.out.websocket.message;

public record PlayerLeaveMessage(
        String username,
        int playerCount
) {}
