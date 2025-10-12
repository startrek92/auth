package com.promptdb.auth.services;

import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.repository.repoInterfaces.BearerTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BearerTokenServiceTest {

    @Mock
    BearerTokenRepository repo;

    @InjectMocks
    BearerTokenService service;

    @Test
    void validateToken_returnsModel_whenFound() throws AuthException {
        BearerTokenModel model = new BearerTokenModel();
        when(repo.getValidBearerToken("jti-1")).thenReturn(model);

        BearerTokenModel result = service.validateToken("jti-1");
        assertSame(model, result);
    }

    @Test
    void validateToken_throws_whenNotFound() {
        when(repo.getValidBearerToken("bad")).thenReturn(null);
        assertThrows(AuthException.class, () -> service.validateToken("bad"));
    }
}
