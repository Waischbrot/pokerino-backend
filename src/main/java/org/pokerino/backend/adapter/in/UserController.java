package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.UserResponse;
import org.pokerino.backend.application.port.in.UserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserUseCase userUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(userUseCase.getMe());
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getUser(@RequestParam String username) {
        return ResponseEntity.ok(userUseCase.getUser(username));
    }

    // Username -> Ändern in DB, Neuen Token generieren und zurückgeben
    @PostMapping("/change-username")
    public ResponseEntity<String> changeUsername(@RequestParam String newUsername) {
        return ResponseEntity.ok(userUseCase.changeUsername(newUsername));
    }

    // Password -> Ändern in dem man das neue hasht und setzt
    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestParam String newPassword) {
        var result = userUseCase.changePassword(newPassword);
        return ResponseEntity.ok(result);
    }
}
