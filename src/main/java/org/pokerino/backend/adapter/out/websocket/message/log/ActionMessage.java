package org.pokerino.backend.adapter.out.websocket.message.log;

import org.pokerino.backend.adapter.out.websocket.message.OutboundMessageType;
import org.pokerino.backend.domain.game.Action;

public record ActionMessage(
        OutboundMessageType type,
        String username,
        Action action,
        long value,
        long currentBet,
        boolean allIn
) {
    public ActionMessage(String username, Action action, long value, long currentBet, boolean allIn) {
        this(OutboundMessageType.LOG_ACTION, username, action, value, currentBet, allIn);
    }
}
