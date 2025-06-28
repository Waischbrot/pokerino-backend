package org.pokerino.backend.adapter.out.websocket.message.log;

import org.pokerino.backend.adapter.out.websocket.message.OutboundMessageType;
import org.pokerino.backend.domain.game.Action;

public record ActionMessage(
        OutboundMessageType type,
        String username,
        Action action,
        long value,
        long currentBet
) {
    public ActionMessage(String username, Action action, long value, long currentBet) {
        this(OutboundMessageType.LOG_ACTION, username, action, value, currentBet);
    }
}
