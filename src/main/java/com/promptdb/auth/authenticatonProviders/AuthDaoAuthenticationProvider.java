package com.promptdb.auth.authenticatonProviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthDaoAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(AuthDaoAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthDaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        logger.info("Authenticating user: {}", username);

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user == null) {
            logger.warn("User '{}' not found", username);
            throw new BadCredentialsException("User not found");
        }

        logger.info("User '{}' loaded successfully", username);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.warn("Invalid password for user '{}'", username);
            throw new BadCredentialsException("Invalid password");
        }

        logger.info("Password validated for user '{}'", username);

        if (!user.isAccountNonLocked()) {
            logger.warn("User '{}' account is locked", username);
            throw new LockedException("Account is locked. Please reset your password.");
        }

        if (!user.isEnabled()) {
            logger.warn("User '{}' account is disabled", username);
            throw new DisabledException("Account is disabled");
        }

        if (!user.isAccountNonExpired()) {
            logger.warn("User '{}' account is expired", username);
            throw new AccountExpiredException("Account has expired");
        }

        if (!user.isCredentialsNonExpired()) {
            logger.warn("User '{}' credentials are expired", username);
            throw new CredentialsExpiredException("Credentials have expired");
        }

        logger.info("Authentication successful for user '{}'", username);

        return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}