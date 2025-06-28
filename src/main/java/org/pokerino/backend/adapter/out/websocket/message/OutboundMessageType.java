package org.pokerino.backend.adapter.out.websocket.message;

public enum OutboundMessageType {
    LOG_ACTION, // A user made a turn -> For logging purposes
    LOG_PLAYER_JOIN, // A new player joined this lobby
    LOG_PLAYER_LEAVE, // A player left this lobby
    START_GAME, // The game started and is no longer joinable
    FINISH_GAME, // The game ended -> This is the winner!
    NEXT_ROUND, // A new round is beginning!
    END_ROUND, // The round ended -> This is the winner of this round / showdown
    NEW_CARDS, // New cards opened on the table
    TURN, // Current turn of the game
    ;
}
