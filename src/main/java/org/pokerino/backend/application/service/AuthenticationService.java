package org.pokerino.backend.application.service;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.adapter.in.dto.VerifyUserDto;
import org.pokerino.backend.application.port.in.AuthenticationUseCase;
import org.pokerino.backend.application.port.in.SendMailUseCase;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    SendMailUseCase sendMailUseCase;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;

    @Override
    public User signup(RegisterUserDto registerUserDto) {
        final User user = new User(registerUserDto.username(), registerUserDto.email(), registerUserDto.password());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return this.saveUserPort.saveUser(user);
    }

    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        final User user = this.loadUserPort.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password())
        );
        return user;
    }

    @Override
    public void verifyUser(VerifyUserDto verifyUserDto) {

    }

    @Override
    public void resendVerificationCode(String email) {

    }

    private void sendVerificationEmail(User user) {
        // Task: Send a nicely formatted HTML email to the user's email address
        // It should contain the users verification code
        // The styling should also contain our companies' logo!

        // Attention: The Email Service can throw an exception incase the SMTP Server is not reachable,
        // catch it here and handle it!
    }

    @Nonnull
    private String generateVerificationCode() {
        int length = (int) (Math.random() * (16 - 12 + 1)) + 12;
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
}