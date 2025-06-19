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

    /**
     * Sign up a new user using the provided credentials.
     * @param registerUserDto Email, Password, Username for this user
     * @return Status & message indicating success or failure
     */
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody RegisterUserDto registerUserDto) {
        try {
            this.authenticationUseCase.signup(registerUserDto);
            return ResponseEntity.ok(true);
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    /**
     * Authenticate a user using the provided credentials.
     * @param loginUserDto Email, Password for this user
     * @return Username and Password for user
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
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
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().body("User could not be logged in");
        }
    }

    /**
     * Check if the given username is already taken.
     * @param username Username to check
     * @return True if the username is taken, false otherwise
     */
    @GetMapping("/username")
    public ResponseEntity<Boolean> username(@RequestParam String username) {
        return ResponseEntity.ok(this.authenticationUseCase.isUsernameTaken(username));
    }

    @GetMapping("/token")
    public ResponseEntity<Boolean> token(@RequestParam String token) {
        return ResponseEntity.ok(this.jwtUseCase.isTokenValid(token));
    }
}