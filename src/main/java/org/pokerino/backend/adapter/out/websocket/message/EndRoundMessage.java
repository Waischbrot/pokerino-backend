package org.pokerino.backend.adapter.out.websocket.message;

import java.util.List;
import java.util.Map;

public record EndRoundMessage(
        OutboundMessageType type,
        List<String> winners,
        long pot,
        Map<String, List<String>> showdownCards
) {
    public EndRoundMessage(List<String> winners, long pot, Map<String, List<String>> showdownCards) {
        this(OutboundMessageType.END_ROUND, winners, pot, showdownCards);
    }
}
