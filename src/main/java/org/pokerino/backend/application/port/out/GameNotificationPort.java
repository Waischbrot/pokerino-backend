package org.pokerino.backend.application.port.out;

import org.pokerino.backend.adapter.out.websocket.message.*;
import org.pokerino.backend.adapter.out.websocket.message.log.ActionMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerJoinMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerLeaveMessage;

public interface GameNotificationPort {
    void notifyAction(String gameCode, ActionMessage message);
    void notifyPlayerLeave(String gameCode, PlayerLeaveMessage message);
    void notifyPlayerJoin(String gameCode, PlayerJoinMessage message);
    void notifyEndRound(String gameCode, EndRoundMessage message);
    void notifyFinishGame(String gameCode, FinishGameMessage message);
    void notifyNewCards(String gameCode, NewCardsMessage message);
    void notifyNextRound(String gameCode, NextRoundMessage message);
    void notifyStartGame(String gameCode, StartGameMessage message);
    void notifyTurn(String gameCode, TurnMessage message);
}
