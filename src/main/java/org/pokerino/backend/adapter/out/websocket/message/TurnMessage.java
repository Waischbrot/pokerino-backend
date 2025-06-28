package org.pokerino.backend.adapter.out.websocket.message;

import org.pokerino.backend.domain.game.Action;

import java.util.List;

public record TurnMessage(
        OutboundMessageType type,
        String username,
        List<Action> actions,
        long currentBet, // THIS IS NOT THE CURRENT BET OF THE PLAYER, BUT THE CURRENT BET OF THE ROUND
        int now, // In Seconds
        int ending // In Seconds
) {
    public TurnMessage(String username,
                       List<Action> actions,
                       long currentBet,
                       int now,
                       int ending) {
        this(OutboundMessageType.TURN, username, actions, currentBet, now, ending);
    }
}
