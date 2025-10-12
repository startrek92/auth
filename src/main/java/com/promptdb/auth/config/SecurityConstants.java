package com.promptdb.auth.config;

import java.util.Arrays;
import java.util.List;

/**
 * Centralized configuration for security-related constants and path exclusions.
 * This ensures consistency between SecurityConfig and JWT filters.
 */
public final class SecurityConstants {
    
    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Paths that should be excluded from JWT authorization checks.
     * These paths will be accessible without any authentication.
     */
    public static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/auth/login",
            "/health"
    );
    
    /**
     * Convert excluded paths to array format for Spring Security configuration.
     */
    public static String[] getExcludedPathsArray() {
        return EXCLUDED_PATHS.toArray(new String[0]);
    }
    
    /**
     * Check if a given path should be excluded from JWT authorization.
     */
    public static boolean isPathExcluded(String path) {
        return EXCLUDED_PATHS.contains(path);
    }
}