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
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    SendMailUseCase sendMailUseCase;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;

    @Override
    public User signup(RegisterUserDto regiserUserDto) {
        return null;
    }

    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        return null;
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