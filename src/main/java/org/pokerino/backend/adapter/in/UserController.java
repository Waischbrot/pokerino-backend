package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.AddExperienceResponse;
import org.pokerino.backend.adapter.in.response.UserResponse;
import org.pokerino.backend.application.port.in.UserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addExperience")
    public ResponseEntity<AddExperienceResponse> addExperience(@RequestParam int experience) {
        return ResponseEntity.ok(userUseCase.addExperience(experience));
    }
}
