package com.promptdb.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.dto.JwtDto;
import com.promptdb.auth.dto.UserLoginRequest;
import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.dto.UserLoginResponseDTO;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.services.BearerTokenService;
import com.promptdb.auth.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final BearerTokenService bearerTokenService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils, BearerTokenService bearerTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        setFilterProcessesUrl("/auth/login"); // this tells spring only invoke this filter when /auth/login is received
        this.bearerTokenService = bearerTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserLoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        String username = authResult.getName();
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        JwtDto jwtDto = jwtUtils.generateTokensForUser((UserModel) userDetails);
        bearerTokenService.saveToken(jwtDto);
        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO((UserModel) userDetails, jwtDto.getAccessToken(), "qwerty");
        ApiResponse<UserLoginResponseDTO> apiResponse = new ApiResponse<>(userLoginResponseDTO);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        Cookie jwtCookie = new Cookie("auth-session-token", jwtDto.getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        ApiResponse<String> apiResponse = new ApiResponse<>("Authentication failed");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }
}