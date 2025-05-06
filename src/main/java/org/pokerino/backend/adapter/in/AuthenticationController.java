package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.adapter.in.response.LoginResponse;
import org.pokerino.backend.application.port.in.AuthenticationUseCase;
import org.pokerino.backend.application.port.in.JWTUseCase;
import org.pokerino.backend.domain.exception.auth.UserAlreadyVerifiedException;
import org.pokerino.backend.domain.exception.auth.UserNotFoundException;
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

    /**
     * Sign up a new user using the provided credentials.
     * @param registerUserDto Email, Password, Username for this user
     * @return Status & message indicating success or failure
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterUserDto registerUserDto) {
        try {
            this.authenticationUseCase.signup(registerUserDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (final Exception exception) {
            return ResponseEntity.internalServerError().body("User could not be registered");
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
            final LoginResponse loginResponse = new LoginResponse(jwtToken, authenticatedUser.getUsername());
            return ResponseEntity.ok(loginResponse);
        } catch (final Exception exception) {
            return ResponseEntity.internalServerError().body("User could not be logged in");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            this.authenticationUseCase.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
            // IllegalStateException: Verification code is invalid
        } catch (UserAlreadyVerifiedException | UserNotFoundException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            this.authenticationUseCase.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
            // IllegalStateException: Mail could not be sent (example: spam)
        } catch (UserAlreadyVerifiedException | UserNotFoundException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/username")
    public ResponseEntity<Boolean> username(@RequestParam String username) {
        return ResponseEntity.ok(this.authenticationUseCase.isUsernameTaken(username));
    }
}