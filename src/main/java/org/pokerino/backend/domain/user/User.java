package org.pokerino.backend.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String username;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String password;

    @Column(name = "verification_code")
    String verificationCode;
    @Column(name = "verification_expiration")
    LocalDateTime verificationCodeExpiresAt;

    @Column(name = "join_date")
    LocalDateTime joinDate;

    @Column
    long chips;
    @Column
    int gold;
    @Column
    long experience;

    boolean enabled;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinDate = LocalDateTime.now();
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}