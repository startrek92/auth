package com.promptdb.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());

        // allow login and health unauthenticated
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/health").permitAll()
                .anyRequest().authenticated());

        httpSecurity.formLogin(form -> form.disable());

        // creates a stateless session, means no sessionID will be set and client
        // has to pass credentials for every request
        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
    }

    @Bean
    public BCrypt bCrypt() {
        return new BCrypt();
    }
}
