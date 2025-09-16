package com.kodsonApp.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class KodsonPrincipal implements UserDetails {
    private Kodson kodson;

    public KodsonPrincipal(Kodson restaurant) {
        this.kodson = restaurant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(this.kodson.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.kodson.getPassword();
    }

    @Override
    public String getUsername() {
        return this.kodson.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.kodson.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.kodson.isActive();
    }

    // Add getter method to access the underlying Kodson user object
    public Kodson getKodson() {
        return this.kodson;
    }
}
