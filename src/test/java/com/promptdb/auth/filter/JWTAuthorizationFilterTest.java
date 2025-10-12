package com.promptdb.auth.filter;

import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.services.BearerTokenService;
import com.promptdb.auth.utils.JWTUtils;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import io.jsonwebtoken.Claims;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private BearerTokenService bearerTokenService;

    private JWTAuthorizationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JWTAuthorizationFilter(jwtUtils, bearerTokenService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void missingAuthorizationHeader_returnsBadRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/any");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(400, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        String body = response.getContentAsString();
        assertTrue(body.contains("failure"));
        assertTrue(body.contains("1003")); // INVALID_AUTH_HEADER code
    }

    @Test
    void validAuthorizationHeader_setsSecurityContext() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/secure");
        String token = "abc.def.ghi";
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        Claims claims = mock(Claims.class);
        when(claims.getId()).thenReturn("jti-123");
        when(claims.getSubject()).thenReturn("alice");
        when(jwtUtils.getAllClaims(token)).thenReturn(claims);

        UserModel user = new UserModel();
        user.setUsername("alice");
        user.setIsActive(true);
        user.setIsLocked(false);

        BearerTokenModel model = new BearerTokenModel();
        model.setUser(user);
        when(bearerTokenService.getToken("jti-123")).thenReturn(model);

        filter.doFilter(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertTrue(principal instanceof UserModel);
        assertEquals("alice", ((UserModel) principal).getUsername());
    }
}
