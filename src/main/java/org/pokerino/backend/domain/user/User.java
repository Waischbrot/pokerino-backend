package org.pokerino.backend.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class User implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

    }

    @Override
    public String getPassword() {

    }

    @Override
    public String getUsername() {

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
