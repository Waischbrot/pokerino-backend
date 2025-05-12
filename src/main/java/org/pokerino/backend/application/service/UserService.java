package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.UserResponse;
import org.pokerino.backend.application.port.in.UserUseCase;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.domain.exception.auth.UserNotFoundException;
import org.pokerino.backend.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class UserService implements UserUseCase {
    LoadUserPort loadUserPort;

    @Override
    public UserResponse getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(username);
    }

    @Override
    public UserResponse getUser(String username) {
        User user = this.loadUserPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.toUserResponse();
    }
}
