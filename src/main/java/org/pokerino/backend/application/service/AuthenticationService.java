package org.pokerino.backend.application.service;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
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
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    SendMailUseCase sendMailUseCase;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;

    // Todo: Check if there is already a User with this Mail (Or test -> the user with email x might not even be created, since db has unique field)
    // Todo: Filter against email domain blacklist for antibot protection
    // Todo: Check if there is already a User with this Username (Or test as described above)
    @Override
    public User signup(RegisterUserDto registerUserDto) {
        final String encodedPassword = this.passwordEncoder.encode(registerUserDto.password());
        final User user = new User(registerUserDto.username(), registerUserDto.email(), encodedPassword);
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1)); // One hour until code expires
        user.setEnabled(false);
        sendVerificationEmail(user);
        return this.saveUserPort.saveUser(user);
    }

    // Needs heavy rework!
    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        final User user = this.loadUserPort.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }
        // Todo: Catch FUCKING AuthenticationException because otherwise your apartment will burn down!

        // Todo: This is kind of bad -> requests user data again, login could be verified right here!
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password())
        ); // -> Maybe UserDetails / User object can be extracted from this?
        return user;
    }

    @Override
    public void verifyUser(VerifyUserDto verifyUserDto) {
        final Optional<User> optionalUser = this.loadUserPort.findByEmail(verifyUserDto.email());
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(verifyUserDto.verificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                this.saveUserPort.saveUser(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        final Optional<User> optionalUser = this.loadUserPort.findByEmail(email);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1)); // One hour until code expires
            sendVerificationEmail(user);
            this.saveUserPort.saveUser(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) {
        final String subject = "Account Verification";
        final String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        final String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to Pokerino!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            this.sendMailUseCase.sendMail(user.getEmail(), subject, htmlMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email");
        }
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