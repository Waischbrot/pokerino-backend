package org.pokerino.backend.adapter.out.websocket.message.log;

import org.pokerino.backend.adapter.out.websocket.message.OutboundMessageType;

public record BlindMessage(
        OutboundMessageType type,
        String smallBlindUsername,
        long smallBlind,
        String bigBlindUsername,
        long bigBlind
) {
    public BlindMessage(String smallBlindUsername, long smallBlind, String bigBlindUsername, long bigBlind) {
        this(OutboundMessageType.BLIND, smallBlindUsername, smallBlind, bigBlindUsername, bigBlind);
    }
}
