package org.pokerino.backend.adapter.out.websocket.message;

import java.util.List;

public record NewCardsMessage(
        OutboundMessageType type,
        List<String> cardsOnTable,
        List<String> newCards
) {
    public NewCardsMessage(List<String> cardsOnTable, List<String> newCards) {
        this(OutboundMessageType.NEW_CARDS, cardsOnTable, newCards);
    }
}
