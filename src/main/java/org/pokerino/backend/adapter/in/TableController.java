package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

import org.pokerino.backend.adapter.in.response.ApiResponse;
import org.pokerino.backend.adapter.out.persistence.GameInMemoryRepository;
import org.pokerino.backend.application.port.in.MatchMakingUseCase;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.game.TableOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableController {

    // Join table -> just send me the code, return is either error or a GameResponse

    // Host table -> Send me all important info, return is either error or a GameResponse

    // Leave table -> No input needed -> returns error if not in a table

    // Current table -> Returns a GameResponse if the user is currently playing, should include available actions if its the players turn!

    // Current table / Available actions -> Returns a list of available actions for the current player if its their turn

    // public tables -> Returns a list of public tables available to join with simple info about them





    MatchMakingUseCase mmUsecase;
    GameInMemoryRepository gameInMemoryRepository;







    // POST /host (Hosts a new table, ?? just possible if you are not already in a table ?? )
    @PostMapping("/host")
    public ApiResponse host(@RequestParam UUID gameId,@RequestParam HostGameRequestDto gameType,@RequestParam long userId){
        PokerGame game = new PokerGame(gameId, new TableOptions(gameType));
        gameInMemoryRepository.saveGame(game);
        mmUsecase.addPlayer(gameId, userId);
        return new ApiResponse("Player has hosted game successfully");
    }

    // POST /join (Joins a table using a code, just possible if you are not already in a table)
    @PostMapping("/join")
    public ApiResponse join(@RequestParam UUID gameId,@RequestParam long userId){
        mmUsecase.addPlayer(gameId, userId);
        return new ApiResponse("User was added successfully");
    }
    

    // POST /leave-queue (Leaves a queue, if present)
    @PostMapping("/leave-queue")
    public ApiResponse leaveQueue(@RequestParam UUID gameId,@RequestParam long userId){
        mmUsecase.deletePlayerFromQueue(gameId, userId);
        return new ApiResponse("User has leaved successufully");
    }

    // POST /leave-game (Leaves a table, if present)
    @PostMapping("/leave-game")
    public ApiResponse leaveGame(@RequestParam UUID gameId,@RequestParam long userId){
        mmUsecase.deletePlayerFromGame(gameId, userId);
        return new ApiResponse("User has leaved successufully");
    }

    // GET /current-game -> Who moves next, who is in the game, everything for displaying the current table
    @GetMapping("/current-game")
    public ApiResponse currentGame(@RequestParam UUID gameId){

        return new ApiResponse("User was added successfully");
    }

    @GetMapping("/is-in-queue")
    public Map<String, Boolean> isInQueue(@RequestParam UUID gameId,@RequestParam long userId ){
        boolean isInQueue = mmUsecase.isInQueue(gameId, userId);
        return Map.of("isInQueue",isInQueue);
    }
    @GetMapping("/get-game")
    public PokerGame getGame(@RequestParam long userId){
        return mmUsecase.getGame(userId);
    }
    @GetMapping("/get-queue-size")
    public int getQueueSize(@RequestParam UUID gameId){
        return mmUsecase.getQueueSize(gameId);
    }

    // GET /public-tables -> Public tables available to join (Just Basic Information)     not needed??


}
