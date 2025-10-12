package com.promptdb.auth.services;

import com.promptdb.auth.dto.UserLoginResponseDTO;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.repoInterfaces.UserRepository;
import com.promptdb.auth.utils.BCryptPasswordEncryptorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncryptorImpl bCrypt;
    @Mock
    JWTService jwtService;

    @InjectMocks
    UserServices userServices;

    private UserModel activeUser;

    @BeforeEach
    void setup() {
        activeUser = new UserModel();
        activeUser.setId(1);
        activeUser.setUsername("alice");
        activeUser.setName("Alice");
        activeUser.setEmail("alice@example.com");
        activeUser.setAge(30);
        activeUser.setIsActive(true);
        activeUser.setIsLocked(false);
        activeUser.setPassword("hashed");
        CompanyModel company = new CompanyModel();
        company.setId(10);
        company.setName("ACME");
        activeUser.setCompanyModel(company);
        activeUser.setCreatedOn(new Date());
        activeUser.setModifiedOn(new Date());
    }

    @Test
    void loginUser_success_returnsResponse() throws Exception {
        when(userRepository.findByUsername("alice")).thenReturn(activeUser);
        when(bCrypt.checkHash("secret", "hashed")).thenReturn(true);
        when(jwtService.generateToken(eq("alice"), any())).thenReturn("jwt-token");
        when(jwtService.generateSessionId(activeUser)).thenReturn("session-1");

        UserLoginResponseDTO dto = userServices.loginUser("alice", "secret");
        assertEquals("jwt-token", dto.getToken());
        assertEquals("alice", dto.getUsername());
        assertEquals(1, dto.getId());
    }

    @Test
    void loginUser_userNotFound_throwsAuthException() {
        when(userRepository.findByUsername("bob")).thenReturn(null);
        assertThrows(AuthException.class, () -> userServices.loginUser("bob", "x"));
    }

    @Test
    void loginUser_lockedUser_throwsAuthException() {
        activeUser.setIsLocked(true);
        when(userRepository.findByUsername("alice")).thenReturn(activeUser);
        assertThrows(AuthException.class, () -> userServices.loginUser("alice", "secret"));
    }

    @Test
    void loginUser_inactiveUser_throwsAuthException() {
        activeUser.setIsActive(false);
        when(userRepository.findByUsername("alice")).thenReturn(activeUser);
        assertThrows(AuthException.class, () -> userServices.loginUser("alice", "secret"));
    }

    @Test
    void loginUser_wrongPassword_throwsAuthException() {
        when(userRepository.findByUsername("alice")).thenReturn(activeUser);
        when(bCrypt.checkHash("wrong", "hashed")).thenReturn(false);
        assertThrows(AuthException.class, () -> userServices.loginUser("alice", "wrong"));
    }
}
