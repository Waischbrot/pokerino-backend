package org.pokerino.backend.adapter.out.websocket.message.log;

import org.pokerino.backend.adapter.out.websocket.message.OutboundMessageType;

public record PlayerJoinMessage(
        OutboundMessageType type,
        String username,
        int playerCount
) {
    public PlayerJoinMessage(String username, int playerCount) {
        this(OutboundMessageType.LOG_PLAYER_JOIN, username, playerCount);
    }
}
