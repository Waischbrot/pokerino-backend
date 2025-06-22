package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.adapter.in.response.ExperienceResponse;
import org.pokerino.backend.adapter.in.response.LoginResponse;
import org.pokerino.backend.adapter.in.response.UserResponse;
import org.pokerino.backend.application.port.in.AuthenticationUseCase;
import org.pokerino.backend.application.port.in.JWTUseCase;
import org.pokerino.backend.application.port.in.LevelUseCase;
import org.pokerino.backend.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class AuthenticationController {
    JWTUseCase jwtUseCase;
    AuthenticationUseCase authenticationUseCase;
    LevelUseCase levelUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody RegisterUserDto registerUserDto) {
        this.authenticationUseCase.signup(registerUserDto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        final User authenticatedUser = this.authenticationUseCase.authenticate(loginUserDto);
        final String jwtToken = this.jwtUseCase.generateToken(authenticatedUser);
        final ExperienceResponse experience = levelUseCase.calculateLevel(authenticatedUser);
        final UserResponse userResponse = new UserResponse(
                authenticatedUser.getUsername(),
                authenticatedUser.getJoinDate(),
                authenticatedUser.getChips(),
                experience
        );
        final LoginResponse loginResponse = new LoginResponse(jwtToken, userResponse);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/username")
    public ResponseEntity<Boolean> username(@RequestParam String username) {
        return ResponseEntity.ok(this.authenticationUseCase.isUsernameTaken(username));
    }

    @GetMapping("/token")
    public ResponseEntity<Boolean> token(@RequestParam String token) {
        return ResponseEntity.ok(this.jwtUseCase.isTokenValid(token));
    }
}