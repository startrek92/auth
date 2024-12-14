package com.promptdb.auth.services;

import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.exceptions.ErrorCodes;
import com.promptdb.auth.models.UserModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@Service
@Getter
@Setter
@NoArgsConstructor
@ToString
public class JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    
    private String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String getUsername(String token) throws AuthException {
        try {
            String username =  getClaimsFromToken(token).getSubject();
            logger.info("token valid for username: {}", username);
            return username;
        } catch (ExpiredJwtException | UnsupportedJwtException
                 | MalformedJwtException | SignatureException
                 | IllegalArgumentException e) {

            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_JWT_TOKEN);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) throws AuthException {
        try {
            final String username = getUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (AuthException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return getClaimsFromToken(token)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            logger.error("Error decoding JWT secret key: {}", e.getMessage());
            throw new JwtException("Error decoding JWT secret key");
        }
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 24 * 60 * 60 * 1000); // 24 hours

        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .id(java.util.UUID.randomUUID().toString())
                .issuer("PromptDB")
                .claim("type", "Bearer");

        if (extraClaims != null && !extraClaims.isEmpty()) {
            extraClaims.forEach(jwtBuilder::claim);
        }

        return jwtBuilder
                .signWith(getKey())
                .compact();
    }

    public String generateToken(String username) {
        return generateToken(username, null);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername(), null);
    }

    public String generateToken(Authentication authentication, Map<String, Object> extraClaims) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername(), extraClaims);
    }

    public String generateSessionId(UserModel user) throws AuthException {
        try {
            String rawSessionData = user.getUsername() + ":" + user.getId() + ":" + System.currentTimeMillis();
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawSessionData.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error generating session ID: {}", e.getMessage());
            throw new AuthException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCodes.INTERNAL_SERVER_ERROR);
        }
    }
}
