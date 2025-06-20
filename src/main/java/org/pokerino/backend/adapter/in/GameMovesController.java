package org.pokerino.backend.adapter.in;

import org.pokerino.backend.adapter.in.response.ApiResponse;
import org.pokerino.backend.application.service.GameMovesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("/move")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class GameMovesController {

    GameMovesService gms;

    @PostMapping("/fold")
    public ApiResponse fold(@RequestParam long playerId ){
        gms.fold(playerId);
        return new ApiResponse("Player has folded successfully");
    }

    @PostMapping("/check")
    public ApiResponse check(@RequestParam long playerId ){
        gms.check(playerId);
        return new ApiResponse("Player has checked successfully");
    }

    @PostMapping("/call")
    public ApiResponse call(@RequestParam long playerId ){
        gms.call(playerId);
        return new ApiResponse("Player has called successfully");
    }

    @PostMapping("/allin")
    public ApiResponse allin(@RequestParam long playerId ){
        gms.allIn(playerId);
        return new ApiResponse("Player put all in successfully");
    }

    @PostMapping("/raise")
    public ApiResponse raise(@RequestParam long playerId, @RequestParam int numberOfChips){
        gms.raise(playerId,numberOfChips);
        return new ApiResponse("Player has raised successfully");
    }
}