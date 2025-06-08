package com.promptdb.auth.utils;

import com.promptdb.auth.dto.JwtDto;
import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.models.UserModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtils {

    private final String SECRET_KEY = "your-secret";
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000; // 15 min
    private final long REFRESH_TOKEN_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000; // 7 days

    public JwtDto generateTokensForUser(UserModel user) {
        String username = user.getUsername();

        // Access token
        Date accessIssuedAt = new Date();
        String accessTokenJti = UUID.randomUUID().toString();
        Date accessExpiresAt = new Date(accessIssuedAt.getTime() + ACCESS_TOKEN_VALIDITY_MS);
        String accessToken = createToken(username, accessIssuedAt, accessExpiresAt, accessTokenJti);

        // Refresh token
        Date refreshIssuedAt = new Date();
        Date refreshExpiresAt = new Date(refreshIssuedAt.getTime() + REFRESH_TOKEN_VALIDITY_MS);
        String refreshToken = UUID.randomUUID().toString();


        return JwtDto.builder()
                .userModel(user)
                .accessToken(accessToken)
                .accessTokenIssuedAt(accessIssuedAt)
                .accessTokenExpiresAt(accessExpiresAt)
                .accessTokenJti(accessTokenJti)
                .accessTokenSubject(username)
                .refreshToken(refreshToken)
                .refreshTokenIssuedAt(refreshIssuedAt)
                .refreshTokenExpiresAt(refreshExpiresAt)
                .refreshTokenSubject(username)
                .build();
    }

    private String createToken(String subject, Date issuedAt, Date expiresAt, String jti) {
        return Jwts.builder().subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .id(jti)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Optional for validation/parsing
    public Date getIssuedAt(String token) {
        return parseClaims(token).getIssuedAt();
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}