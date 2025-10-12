package com.promptdb.auth.services;

import com.promptdb.auth.dto.JwtDto;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.exceptions.ErrorCodes;
import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.repository.repoInterfaces.BearerTokenRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Service
public class BearerTokenService {

    @Autowired
    private BearerTokenRepository bearerTokenRepository;

    @Transactional
    public void saveToken(JwtDto jwtDto) {
        BearerTokenModel bearerTokenModel = new BearerTokenModel(jwtDto);
        bearerTokenRepository.save(bearerTokenModel);
    }

    @Transactional
    public BearerTokenModel getToken(String accessToken) throws AuthException {
        BearerTokenModel bearerTokenModel = bearerTokenRepository.getValidBearerToken(accessToken);
        if (bearerTokenModel == null) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_JWT_TOKEN);
        }
        return bearerTokenModel;
    }
}
