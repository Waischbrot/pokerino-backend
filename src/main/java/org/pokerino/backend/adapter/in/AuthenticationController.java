package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.adapter.in.response.LoginResponse;
import org.pokerino.backend.application.port.in.AuthenticationUseCase;
import org.pokerino.backend.application.port.in.JWTUseCase;
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

    /**
     * Verify a user using the provided verification code.
     * @param verificationCode Verification code for this user
     * @return Status & message indicating success or failure
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String verificationCode) {
        try {
            this.authenticationUseCase.verifyUser(verificationCode);
            return ResponseEntity.ok("Account verified successfully");
        } catch (final Exception exception) {
            return ResponseEntity.internalServerError().body("User could not be verified");
        }
    }

    /**
     * Resend the verification code to the user.
     * @param email Email of the user
     * @return Status & message indicating success or failure
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            this.authenticationUseCase.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (final Exception exception) {
            return ResponseEntity.internalServerError().body("Verification code could not be sent");
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
}