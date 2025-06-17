package org.pokerino.backend.adapter.in;

import java.util.Map;
import java.util.UUID;

import org.pokerino.backend.adapter.in.response.ApiResponse;
import org.pokerino.backend.application.port.in.MatchMakingUseCase;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.game.Table;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/matchMaking")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class MatchMakingController {
    MatchMakingUseCase mmUsecase;

@PostMapping("/add-player-by-game")
public ApiResponse addPlayer(@RequestParam UUID gameId,@RequestParam long userId){
    mmUsecase.addPlayer(gameId,userId);
    return new ApiResponse("User was added successfully");
}

@PostMapping("/add-player-by-table")
public ApiResponse addPlayer(@RequestParam Table table,@RequestParam long userId){
    mmUsecase.addPlayer(table,userId);
    return new ApiResponse("User was added successfully");
}
@PostMapping("/delete-player-by-game")
public ApiResponse deletePlayer(@RequestParam UUID gameId,@RequestParam long userId){
    mmUsecase.addPlayer(gameId,userId);
    return new ApiResponse("User was deleted successfully");
}

@PostMapping("/delete-player-by-table")
public ApiResponse deletePlayer(@RequestParam Table table,@RequestParam long userId){
    mmUsecase.addPlayer(table,userId);
    return new ApiResponse("User was deleted successfully");
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

}
