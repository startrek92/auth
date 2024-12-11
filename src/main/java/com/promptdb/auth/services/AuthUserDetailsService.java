package com.promptdb.auth.services;

import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.models.UserPrincipalModel;
import com.promptdb.auth.repository.UserRepository;
import jdk.jfr.Unsigned;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("invalid username");
        }
        return new UserPrincipalModel(user);
    }
}
