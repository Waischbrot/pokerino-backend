package org.pokerino.backend.adapter.out.persistence;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.application.port.out.CountUsersPort;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort, CountUsersPort {
    UserJPARepository userJPARepository;

    @Override
    public Optional<User> findById(long id) {
        return this.userJPARepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userJPARepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userJPARepository.findByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return this.userJPARepository.save(user);
    }

    @Override
    public long countUsers() {
        return this.userJPARepository.count();
    }
}