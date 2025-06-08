package com.promptdb.auth.services;

import com.promptdb.auth.dto.JwtDto;
import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.repository.repoInterfaces.BearerTokenRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
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
}
