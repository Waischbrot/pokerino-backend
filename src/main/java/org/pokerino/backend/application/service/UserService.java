package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.ExperienceResponse;
import org.pokerino.backend.adapter.in.response.UserResponse;
import org.pokerino.backend.application.port.in.JWTUseCase;
import org.pokerino.backend.application.port.in.LevelUseCase;
import org.pokerino.backend.application.port.in.UserUseCase;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.pokerino.backend.domain.outbound.exception.InternalServerErrorException;
import org.pokerino.backend.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class UserService implements UserUseCase {
    PasswordEncoder passwordEncoder;
    LoadUserPort loadUserPort;
    LevelUseCase levelUseCase;
    JWTUseCase jwtUseCase;
    SaveUserPort saveUserPort;

    @Override
    public UserResponse getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(username);
    }

    @Override
    public UserResponse getUser(String username) {
        User user = this.loadUserPort.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
        ExperienceResponse experience = this.levelUseCase.calculateLevel(user);
        return new UserResponse(
                user.getUsername(),
                user.getJoinDate(),
                user.getChips(),
                experience
        );
    }

    @Override
    public String changeUsername(String newUsername) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = loadUserPort.findByUsername(username)
            .orElseThrow(() -> new InternalServerErrorException("User not found"));
        user.setUsername(newUsername);
        saveUserPort.saveUser(user);
        final String jwtToken = this.jwtUseCase.generateToken(user);
        return jwtToken;
    }

    @Override
    public boolean changePassword(String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = loadUserPort.findByUsername(username)
            .orElseThrow(() -> new InternalServerErrorException("User not found"));
        final String endcodedPassword = this.passwordEncoder.encode(newPassword);
        user.setPassword(endcodedPassword);
        saveUserPort.saveUser(user);
        return true;
    }
}
