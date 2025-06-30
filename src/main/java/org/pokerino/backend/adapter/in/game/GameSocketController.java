package org.pokerino.backend.adapter.in.game;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.game.TurnMessage;
import org.pokerino.backend.application.port.in.JWTUseCase;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.GameState;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GameSocketController {
    TurnUseCase turnUseCase;
    LoadGamePort loadGamePort;
    JWTUseCase jwtUseCase;

    @MessageMapping("/game/turn")
    public void handleTurn(final TurnMessage message) {
        final String username = getUsername(message.token());
        final PokerGame game = loadGamePort.getUserGame(username)
                .orElseThrow(() -> new BadRequestException("No game found for user: " + username));
        if (game.getState() != GameState.IN_ROUND) {
            throw new BadRequestException("Game is not in a valid state for turns: " + game.getState());
        }
        final GamePlayer player = game.getPlayer(username);

        switch (message.action()) {
            case FOLD -> turnUseCase.fold(game, player);
            case CHECK -> turnUseCase.check(game, player);
            case CALL -> turnUseCase.call(game, player);
            case RAISE -> turnUseCase.raise(game, player, message.chips());
            case ALL_IN -> turnUseCase.allIn(game, player);
        }
    }

    @NonNull
    private String getUsername(String token) {
        try {
            return jwtUseCase.extractUsername(token);
        } catch (Exception exception) {
            throw new BadRequestException("Invalid or missing JWT token");
        }
    }
}
