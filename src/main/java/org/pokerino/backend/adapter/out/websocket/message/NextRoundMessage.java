package org.pokerino.backend.adapter.out.websocket.message;

import java.util.List;
import java.util.Map;

public record NextRoundMessage(
        OutboundMessageType type,
        String winner,
        long pot,
        Map<String, List<String>> showdownCards,
        TurnMessage turn
) {
    public NextRoundMessage(String winner,
                            long pot,
                            Map<String, List<String>> showdownCards,
                            TurnMessage turn) {
        this(OutboundMessageType.NEXT_ROUND, winner, pot, showdownCards, turn);
    }
}
