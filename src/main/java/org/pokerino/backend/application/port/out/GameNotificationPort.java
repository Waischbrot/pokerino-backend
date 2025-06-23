package org.pokerino.backend.application.port.out;

import org.pokerino.backend.adapter.out.websocket.message.FinishGameMessage;
import org.pokerino.backend.adapter.out.websocket.message.NextBetMessage;
import org.pokerino.backend.adapter.out.websocket.message.NextRoundMessage;
import org.pokerino.backend.adapter.out.websocket.message.TurnMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.ActionMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.BlindMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerJoinMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerLeaveMessage;

public interface GameNotificationPort {
    void notifyPlayerLeave(String gameCode, PlayerLeaveMessage message);
    void notifyPlayerJoin(String gameCode, PlayerJoinMessage message);
    void notifyBlind(String gameCode, BlindMessage message);
    void notifyAction(String gameCode, ActionMessage message);
    void notifyFinishGame(String gameCode, FinishGameMessage message);
    void notifyNextBet(String gameCode, NextBetMessage message);
    void notifyNextRound(String gameCode, NextRoundMessage message);
    void notifyTurn(String gameCode, TurnMessage message);

}
