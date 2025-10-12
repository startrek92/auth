package com.promptdb.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.config.SecurityConstants;
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

        String path = getRequestPath(request);
        log.info("Started JWT authorization for path: {}", path);

        AuthException authException = null;
        Claims claims = null;
        BearerTokenModel bearerTokenModel = null;
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            log.warn("Missing or invalid Authorization header");
            authException = new AuthException(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_AUTH_HEADER);
        } else {
            String authToken = authHeader.substring(7);
            log.debug("Received Bearer token: {}", authToken);

            try {
                claims = jwtUtils.getAllClaims(authToken);
                log.debug("Parsed JWT claims: subject={}, id={}, issuedAt={}, expiration={}",
                        claims.getSubject(), claims.getId(), claims.getIssuedAt(), claims.getExpiration());

                bearerTokenModel = bearerTokenService.validateToken(claims.getId());

                if (!bearerTokenModel.getUser().getUsername().equals(claims.getSubject())) {
                    log.warn("JWT subject does not match user in token service. Subject: {}, Expected: {}",
                            claims.getSubject(), bearerTokenModel.getUser().getUsername());
                    authException = new AuthException(HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_JWT_TOKEN);
                } else {
                    log.info("JWT token validated successfully for user: {}", claims.getSubject());
                }

            } catch (Exception e) {
                log.error("Error validating JWT token", e);
                authException = new AuthException(HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_JWT_TOKEN);
            }
        }

        if (authException != null) {
            log.info("JWT authorization failed, sending error response: {}", authException.getErrorCode());
            ApiResponse<?> apiResponse = authException.generateResponse();
            response.setStatus(authException.getHttpStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), apiResponse);
            return;
        }

        // Set authentication context
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        bearerTokenModel.getUser(),
                        null,
                        bearerTokenModel.getUser().getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Security context set for user: {}", bearerTokenModel.getUser().getUsername());

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = getRequestPath(request);
        boolean skip = SecurityConstants.isPathExcluded(path);
        if (skip) log.info("Skipping JWT filter for excluded endpoint: {}", path);
        return skip;
    }
    
    private String getRequestPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String requestURI = request.getRequestURI();
        
        // If servlet path is empty, use request URI
        if (servletPath == null || servletPath.isEmpty()) {
            return requestURI;
        }
        
        // If pathInfo exists, combine servlet path with path info
        if (pathInfo != null) {
            return servletPath + pathInfo;
        }
        
        return servletPath;
    }
}
