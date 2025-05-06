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
public class AuthenticationController {
    JWTUseCase jwtUseCase;
    AuthenticationUseCase authenticationUseCase;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterUserDto registerUserDto) {
        try {
            this.authenticationUseCase.signup(registerUserDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("User could not be registered");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            final User authenticatedUser = this.authenticationUseCase.authenticate(loginUserDto);
            final String jwtToken = this.jwtUseCase.generateToken(authenticatedUser);
            final LoginResponse loginResponse = new LoginResponse(jwtToken, this.jwtUseCase.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) { // Todo: Replace against specific exceptions
            return ResponseEntity.badRequest().body(e.getMessage());
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