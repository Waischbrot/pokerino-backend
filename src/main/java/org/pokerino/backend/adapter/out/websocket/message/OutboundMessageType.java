package org.pokerino.backend.adapter.out.websocket.message;

public enum OutboundMessageType {
    ACTION,
    PLAYER_JOIN,
    PLAYER_LEAVE,
    FINISH_GAME,
    NEXT_ROUND,
    NEXT_BET,
    TURN,
    BLIND,
    ;
}
