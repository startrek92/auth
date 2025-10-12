package com.promptdb.auth.config;

import com.promptdb.auth.authenticatonProviders.AuthDaoAuthenticationProvider;
import com.promptdb.auth.filter.JWTAuthenticationFilter;
import com.promptdb.auth.filter.JWTAuthorizationFilter;
import com.promptdb.auth.services.BearerTokenService;
import com.promptdb.auth.utils.JWTUtils;
import com.promptdb.auth.services.AuthUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // === Core Beans ===

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(AuthUserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        return new AuthDaoAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider));
    }

    // === Main Security Filter Chain ===

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   JWTUtils jwtUtils, BearerTokenService bearerTokenService) throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtils, bearerTokenService);
        JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(jwtUtils, bearerTokenService);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/health").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").anonymous()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, JWTAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((req, res, ex) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
                        )
                );

        return http.build();
    }

    // === CORS Configuration ===

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry cors) {
                cors.addMapping("/**")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedOriginPatterns("*");
            }
        };
    }
}