package org.pokerino.backend.adapter.out.persistence;

import org.pokerino.backend.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJPARepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);
}
