package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.application.port.in.game.GameUseCase;
import org.pokerino.backend.application.port.out.GameNotificationPort;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class GameService implements GameUseCase {
    ManageGamePort manageGamePort;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;
    GameNotificationPort gameNotificationPort;

    @Override
    public void win(final PokerGame game, final GamePlayer player) {

    }
}
