package org.pokerino.backend.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {

    }

    @Override
    public boolean isAccountNonLocked() {

    }

    @Override
    public boolean isCredentialsNonExpired() {

    }

    @Override
    public boolean isEnabled() {

    }
}
