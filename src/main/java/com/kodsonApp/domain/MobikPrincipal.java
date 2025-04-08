package com.kodsonApp.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class MobikPrincipal implements UserDetails {
    private Mobik mobik;

    public MobikPrincipal(Mobik restaurant) {
        this.mobik = restaurant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(this.mobik.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.mobik.getPassword();
    }

    @Override
    public String getUsername() {
        return this.mobik.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.mobik.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.mobik.isActive();
    }
}
