package com.orion.serviceuser.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken implements Authentication {
    
    private final String principal;
    private final String token;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Long userId;
    private final String role;
    private boolean authenticated;

    public JwtAuthenticationToken(String principal, String token, 
                                  Collection<? extends GrantedAuthority> authorities,
                                  Long userId, String role) {
        this.principal = principal;
        this.token = token;
        this.authorities = authorities;
        this.userId = userId;
        this.role = role;
        this.authenticated = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
