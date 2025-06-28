package org.pokerino.backend.adapter.out.websocket.message;

public record StartGameMessage(
        OutboundMessageType type,
        int playerCount
) {
    public StartGameMessage(int playerCount) {
        this(OutboundMessageType.START_GAME, playerCount);
    }
}