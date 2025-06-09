package com.promptdb.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.exceptions.ErrorCodes;
import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.services.BearerTokenService;
import com.promptdb.auth.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    private final JWTUtils jwtUtils;
    private final BearerTokenService bearerTokenService;

    public JWTAuthorizationFilter(JWTUtils jwtUtils, BearerTokenService bearerTokenService) {
        this.jwtUtils = jwtUtils;
        this.bearerTokenService = bearerTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("started authorization for {} ", request.getServletPath());
        AuthException authException = null;
        Claims claims = null;
        BearerTokenModel bearerTokenModel = null;
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            authException = new AuthException(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_AUTH_HEADER);
        }

        if (authException == null) {
            String authToken = authHeader.substring(7);
            try {
                claims = jwtUtils.getAllClaims(authToken);
                bearerTokenModel = bearerTokenService.validateToken(claims.getId());
                if (!bearerTokenModel.getUser().getUsername().equals(claims.getSubject())) {
                    authException = new AuthException(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_JWT_TOKEN);
                }

            } catch (Exception e) {
                authException = new AuthException(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_JWT_TOKEN);
            }
        }

        if (authException != null) {
            ApiResponse<?> apiResponse = authException.generateResponse();
            response.setStatus(authException.getHttpStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), apiResponse);
            return;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                bearerTokenModel.getUser(), null, bearerTokenModel.getUser().getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        log.info("skipped filter for jwt auth filter, received login endpoint");
        return request.getServletPath().equals("/auth/login");
    }
}
