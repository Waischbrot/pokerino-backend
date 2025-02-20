package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.adapter.in.dto.VerifyUserDto;
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
public class AuthenticationController {
    JWTUseCase jwtUseCase;
    AuthenticationUseCase authenticationUseCase;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisterUserDto registerUserDto) {
        final User registeredUser = this.authenticationUseCase.signup(registerUserDto);
        // Todo: Handle errors & other responses - for example what if account with email already exists? Or other things!
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        final User authenticatedUser = this.authenticationUseCase.authenticate(loginUserDto);
        final String jwtToken = this.jwtUseCase.generateToken(authenticatedUser);
        final LoginResponse loginResponse = new LoginResponse(jwtToken, this.jwtUseCase.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            this.authenticationUseCase.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            this.authenticationUseCase.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}