package org.pokerino.backend.adapter.out.websocket.message;

import java.util.List;

public record NextBetMessage(
        OutboundMessageType type,
        List<String> cardsOnTable,
        List<String> newCards,
        TurnMessage turn
) {
    public NextBetMessage(List<String> cardsOnTable,
                          List<String> newCards,
                          TurnMessage turn) {
        this(OutboundMessageType.NEXT_BET, cardsOnTable, newCards, turn);
    }
}
