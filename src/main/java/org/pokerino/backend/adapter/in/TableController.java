package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.HostGameDto;
import org.pokerino.backend.adapter.in.response.ActionsResponse;
import org.pokerino.backend.adapter.in.response.GameResponse;
import org.pokerino.backend.application.port.in.TableUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableController {
    TableUseCase tableUseCase;

    // Host table -> Send me all important info, return is either error or a GameResponse
    @PostMapping("/host")
    public ResponseEntity<GameResponse> host(@RequestBody HostGameDto hostGameDto) {
        var result = tableUseCase.host(hostGameDto);
        return ResponseEntity.ok(result);
    }

    // Join table -> just send me the code, return is either error or a GameResponse
    @PostMapping("/join")
    public ResponseEntity<GameResponse> join(@RequestParam String code) {
        var result = tableUseCase.join(code);
        return ResponseEntity.ok(result);
    }

    // Leave table -> No input needed -> returns error if not in a table
    @PostMapping("/leave")
    public void leave() {
        tableUseCase.leave();
    }

    // Current table -> Returns a GameResponse if the user is currently playing
    // should include available actions if its the players turn!
    @GetMapping("/current")
    public ResponseEntity<GameResponse> current() {
        var result = tableUseCase.current();
        return ResponseEntity.ok(result);
    }

    // Current table / Available actions -> Returns a list of available actions
    // for the current player if its their turn
    @GetMapping("current/actions")
    public ResponseEntity<ActionsResponse> currentActions() {
        var result = tableUseCase.currentActions();
        return ResponseEntity.ok(result);
    }
}
