package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.game.HostGameDto;
import org.pokerino.backend.adapter.in.response.game.ActionsResponse;
import org.pokerino.backend.adapter.in.response.game.GameResponse;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerJoinMessage;
import org.pokerino.backend.adapter.out.websocket.message.log.PlayerLeaveMessage;
import org.pokerino.backend.application.port.in.game.GameUseCase;
import org.pokerino.backend.application.port.in.game.GetGameUseCase;
import org.pokerino.backend.application.port.in.game.TableUseCase;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.application.port.out.*;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.GameState;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.game.TableOptions;
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
    GameNotificationPort gameNotificationPort;
    TurnUseCase turnUseCase;
    GameUseCase gameUseCase;

    @Override
    public GameResponse host(HostGameDto hostGameDto) {
        // Check the old game of the user, if it exists and is not dead.
        final String username = getUsername();
        final Optional<PokerGame> userGame = loadGamePort.getUserGame(username);
        userGame.ifPresent(game -> {
            if (!game.getPlayer(username).isDead()) {
                throw new BadRequestException("You are already in a game and not dead yet!");
            }
        });

        // Validate the game options.
        final TableOptions options = TableOptions.fromRequest(hostGameDto);

        // Create a new game with the given options.
        final PokerGame pokerGame = new PokerGame(manageGamePort.generateGameCode(), options);

        // Load the new user and check if he has enough chips -> deduct them.
        final User user = loadUserPort.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found: " + username));
        if (user.getChips() < options.getStartBalance()) {
            throw new BadRequestException("User: '" + username + "' does not have enough chips to host this game!");
        }
        user.setChips(user.getChips() - options.getStartBalance());
        saveUserPort.saveUser(user);

        // Persist the new game.
        manageGamePort.saveGame(pokerGame);

        // Remove user from old game if it exists
        userGame.ifPresent(game -> {
            game.removePlayer(username);
        });

        // Add the player to the new game
        pokerGame.addPlayer(user);

        // Start the scheduler for the game.
        gameUseCase.startScheduler(pokerGame);

        // Return the game response
        return getGameUseCase.toResponse(pokerGame, username);
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

        // Notify the websocket that the user joined the game. -> For other players to update their lobbies.
        gameNotificationPort.notifyPlayerJoin(pokerGame.getGameCode(), new PlayerJoinMessage(username, pokerGame.playerCount()));

        // Return the game response
        return getGameUseCase.toResponse(pokerGame, username);
    }

    @Override
    public void leave() {
        // Check for and find the users existing game.
        final String username = getUsername();
        final PokerGame pokerGame = loadGamePort.getUserGame(username)
                .orElseThrow(() -> new BadRequestException("No game found for user: " + username));

        if (pokerGame.getState() == GameState.WAITING_FOR_PLAYERS) {
            // Remove the player from the game.
            pokerGame.removePlayer(username);

            // Find user and return the chips.
            final User user = loadUserPort.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("User not found: " + username));
            user.setChips(user.getChips() + pokerGame.getOptions().getStartBalance());
            saveUserPort.saveUser(user);

            // Check if the game is empty and delete it if so.
            if (pokerGame.playerCount() == 0) {
                manageGamePort.removeGame(pokerGame.getGameCode());
            } else {
                gameNotificationPort.notifyPlayerLeave(pokerGame.getGameCode(), new PlayerLeaveMessage(username, pokerGame.playerCount()));
            }
            return;
        }

        // Check if its the turn of this user -> if so, skip the turn and notify the Websocket.
        final GamePlayer player = pokerGame.getPlayer(username);
        if (pokerGame.getCurrent().equals(player)) {
            // Skip the turn of the player by folding.
            turnUseCase.fold(pokerGame, player);
        }

        // Other logic like setting the player to be dead.
        player.setDead(true);
        player.setFolded(true); // Fold anyways. !!! Be careful when calculating the pot, don't skip the dead players.

        // Notify the Websocket that the user left the game.
        gameNotificationPort.notifyPlayerLeave(pokerGame.getGameCode(), new PlayerLeaveMessage(username, pokerGame.playerCount()));

        // Check if only one player remains in the game -> We can end it here!
        final int count = pokerGame.aliveCount();
        if (count == 1) {
            // Last player won, handle and then delete the game.
            this.gameUseCase.win(pokerGame, pokerGame.getCurrent());
        } else if (count == 0) {
            manageGamePort.removeGame(pokerGame.getGameCode());
        }
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
