package com.promptdb.auth.utils;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
public class BCryptPasswordEncryptorImpl implements PasswordEncryptor {

    @Override
    public String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public Boolean checkHash(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
