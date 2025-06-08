package com.promptdb.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

// this class tells that this is the authentication type our customer
// authentication provider will be handling
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private String token;

    public JWTAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }

    public String getToken() {
        return this.token;
    }
}
