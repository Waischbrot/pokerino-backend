package org.pokerino.backend.adapter.out.websocket;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.out.websocket.message.*;
import org.pokerino.backend.adapter.out.websocket.message.log.ActionMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerJoinMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerLeaveMessage;
import org.pokerino.backend.application.port.out.GameNotificationPort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class GameNotificationAdapter implements GameNotificationPort {
    SimpMessagingTemplate messagingTemplate;

    @NonNull
    private String getDestination(String gameCode) {
        return "/topic/game/" + gameCode;
    }

    private void notify(String gameCode, Object message) {
        messagingTemplate.convertAndSend(getDestination(gameCode), message);
    }

    @Override
    public void notifyPlayerLeave(String gameCode, PlayerLeaveMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyPlayerJoin(String gameCode, PlayerJoinMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyEndRound(String gameCode, EndRoundMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyAction(String gameCode, ActionMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyFinishGame(String gameCode, FinishGameMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyNewCards(String gameCode, NewCardsMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyNextRound(String gameCode, NextRoundMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyStartGame(String gameCode, StartGameMessage message) {
        notify(gameCode, message);
    }

    @Override
    public void notifyTurn(String gameCode, TurnMessage message) {
        notify(gameCode, message);
    }
}