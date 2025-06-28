package org.pokerino.backend.adapter.out.websocket.message;

import java.util.List;
import java.util.Map;

public record EndRoundMessage(
        OutboundMessageType type,
        String winner,
        long pot,
        Map<String, List<String>> showdownCards
) {
    public EndRoundMessage(String winner, long pot, Map<String, List<String>> showdownCards) {
        this(OutboundMessageType.END_ROUND, winner, pot, showdownCards);
    }
}
