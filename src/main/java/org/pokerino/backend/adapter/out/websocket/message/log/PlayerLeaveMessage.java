package org.pokerino.backend.adapter.out.websocket.message.log;

import org.pokerino.backend.adapter.out.websocket.message.OutboundMessageType;

public record PlayerLeaveMessage(
        OutboundMessageType type,
        String username,
        int playerCount
) {
    public PlayerLeaveMessage(String username, int playerCount) {
        this(OutboundMessageType.PLAYER_LEAVE, username, playerCount);
    }
}
