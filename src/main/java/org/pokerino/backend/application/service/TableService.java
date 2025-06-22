package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.game.HostGameDto;
import org.pokerino.backend.adapter.in.response.game.ActionsResponse;
import org.pokerino.backend.adapter.in.response.game.GameResponse;
import org.pokerino.backend.application.port.in.GetGameUseCase;
import org.pokerino.backend.application.port.in.TableUseCase;
import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableService implements TableUseCase {
    GetGameUseCase getGameUseCase;
    ManageGamePort manageGamePort;
    LoadGamePort loadGamePort;

    @Override
    public GameResponse host(HostGameDto hostGameDto) {
        return null;
        // TODO: Check if the player has an active game in which he is NOT dead.
    }

    @Override
    public GameResponse join(String code) {
        return null;
        // TODO: Check if the player has an active game in which he is NOT dead.
    }

    @Override
    public void leave() {
        final String username = getUsername();
        final PokerGame pokerGame = loadGamePort.getUserGame(username)
                .orElseThrow(() -> new BadRequestException("No game found for user: " + username));
        // TODO: Find if it is this users turn. If it is, then skip and notify the Websocket.
        // TODO: Set the player to dead next up. He can start another game anytime if that is done.

        // TODO: All his chips just stay in the game, his pot stays as well. In the end the winner will get them like everyone elses.

        // TODO: If still queuing, completely remove the player from the game and pay back the chips.

        // TODO: If the game is already over, then handle it accordingly - no extra punishment.
    }

    @Override
    public GameResponse current() {
        final String username = getUsername();
        final PokerGame pokerGame = loadGamePort.getUserGame(username)
                .orElseThrow(() -> new BadRequestException("No game found for user: " + username));
        return getGameUseCase.toResponse(pokerGame, username);
    }

    @Override
    public ActionsResponse currentActions() {
        final String username = getUsername();
        final PokerGame pokerGame = loadGamePort.getUserGame(username)
                .orElseThrow(() -> new BadRequestException("No game found for user: " + username));
        return getGameUseCase.availableActions(pokerGame, username);
    }

    @NonNull
    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
