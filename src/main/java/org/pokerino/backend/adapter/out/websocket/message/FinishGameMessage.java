package org.pokerino.backend.adapter.out.websocket.message;

public record FinishGameMessage(
        OutboundMessageType type,
        String winner,
        long total
) {
    public FinishGameMessage(String winner, long total) {
        this(OutboundMessageType.FINISH_GAME, winner, total);
    }
}
