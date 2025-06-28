package org.pokerino.backend.adapter.out.websocket.message;

public record NextRoundMessage(
        OutboundMessageType type,
        String smallBlindUsername,
        long smallBlind,
        String bigBlindUsername,
        long bigBlind
) {
    public NextRoundMessage(String smallBlindUsername, long smallBlind, String bigBlindUsername, long bigBlind) {
        this(OutboundMessageType.NEXT_ROUND, smallBlindUsername, smallBlind, bigBlindUsername, bigBlind);
    }
}
