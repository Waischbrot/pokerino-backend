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
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.game.GameState;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.pokerino.backend.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableService implements TableUseCase {
    GetGameUseCase getGameUseCase;
    ManageGamePort manageGamePort;
    LoadGamePort loadGamePort;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;

    @Override
    public GameResponse host(HostGameDto hostGameDto) {
        return null;
        // TODO: Check if the player has an active game in which he is NOT dead.
    }

    @Override
    public GameResponse join(String code) {
        // Check the old game of the user, if it exists and is not dead.
        final String username = getUsername();
        final Optional<PokerGame> userGame = loadGamePort.getUserGame(username);
        userGame.ifPresent(game -> {
            if (!game.getPlayer(username).isDead()) {
                throw new BadRequestException("You are already in a game and not dead yet!");
            }
        });

        // Load the game with the given code.
        final PokerGame pokerGame = loadGamePort.getGame(code)
                .orElseThrow(() -> new BadRequestException("No game found with code: " + code));

        // Check if the game is in a state to join.
        if (pokerGame.getState() != GameState.WAITING_FOR_PLAYERS) {
            throw new BadRequestException("Game: '" + code + "' is not in a state to join!");
        }

        // Check if the game is full.
        if (pokerGame.playerCount() >= pokerGame.getOptions().getMaxPlayers()) {
            throw new BadRequestException("Game: '" + code + "' is full!");
        }

        // Load the new user and check if he has enough chips -> deduct them.
        final User user = loadUserPort.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found: " + username));
        if (user.getChips() < pokerGame.getOptions().getStartBalance()) {
            throw new BadRequestException("User: '" + username + "' does not have enough chips to join the game!");
        }
        user.setChips(user.getChips() - pokerGame.getOptions().getStartBalance());
        saveUserPort.saveUser(user);

        // Remove from old game if it exists
        userGame.ifPresent(game -> {
            game.removePlayer(username);
        });

        // Add the player to the new game
        pokerGame.addPlayer(user);

        // Return the game response
        return getGameUseCase.toResponse(pokerGame, username);
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

        // TODO: If all players leave, then delete the game.
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
