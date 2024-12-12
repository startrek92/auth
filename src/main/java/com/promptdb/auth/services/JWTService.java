package com.promptdb.auth.services;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
@ToString
public class JWTService {

    public String getUsername(String token) {
        return "";
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return true;
    }
}
