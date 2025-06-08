package com.promptdb.auth.dto;

import com.promptdb.auth.models.UserModel;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {

    private UserModel userModel;

    private String accessToken;
    private Date accessTokenIssuedAt;
    private Date accessTokenExpiresAt;
    private String accessTokenSubject;
    private String accessTokenJti;

    private String refreshToken;
    private Date refreshTokenIssuedAt;
    private Date refreshTokenExpiresAt;
    private String refreshTokenSubject;
}