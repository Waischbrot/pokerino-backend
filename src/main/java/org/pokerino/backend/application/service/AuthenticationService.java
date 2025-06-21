package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.application.port.in.AuthenticationUseCase;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.outbound.exception.InternalServerErrorException;
import org.pokerino.backend.domain.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;

    @Override
    public void signup(RegisterUserDto registerUserDto) {
        if (this.loadUserPort.findByEmail(registerUserDto.email()).isPresent()) {
            throw new InternalServerErrorException("Email already taken");
        }
        if (isUsernameTaken(registerUserDto.username())) {
            throw new InternalServerErrorException("Username already taken");
        }
        final String encodedPassword = this.passwordEncoder.encode(registerUserDto.password());
        final User user = new User(registerUserDto.username(), registerUserDto.email(), encodedPassword);
        this.saveUserPort.saveUser(user);
    }

    // Needs heavy rework!
    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        final User user = this.loadUserPort.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new InternalServerErrorException("User not found"));
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), loginUserDto.password())
            );
        } catch (final Exception exception) {
            throw new InternalServerErrorException("Authentication failed!");
        }
        return user;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return this.loadUserPort.findByUsername(username).isPresent();
    }
}