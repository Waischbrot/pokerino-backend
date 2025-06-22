package org.pokerino.backend.application.port.out;

import org.pokerino.backend.adapter.out.websocket.message.PlayerJoinMessage;
import org.pokerino.backend.adapter.out.websocket.message.PlayerLeaveMessage;

public interface GameNotificationPort {
    void notifyPlayerLeave(String gameCode, PlayerLeaveMessage message);
    void notifyPlayerJoin(String gameCode, PlayerJoinMessage message);

}
