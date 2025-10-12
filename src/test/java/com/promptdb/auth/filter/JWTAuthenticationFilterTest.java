package com.promptdb.auth.filter;

import com.promptdb.auth.dto.JwtDto;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.services.BearerTokenService;
import com.promptdb.auth.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JWTUtils jwtUtils;
    @Mock
    BearerTokenService bearerTokenService;

    @Test
    void attemptAuthentication_readsCredentialsAndDelegatesToAuthManager() {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager, jwtUtils, bearerTokenService);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent("{\"username\":\"alice\",\"password\":\"secret\",\"state\":\"x\"}".getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("alice", null, List.of(new SimpleGrantedAuthority("USER")))
        );

        Authentication auth = filter.attemptAuthentication(request, response);
        assertNotNull(auth);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        UsernamePasswordAuthenticationToken passed = captor.getValue();
        assertEquals("alice", passed.getPrincipal());
        assertEquals("secret", passed.getCredentials());
    }

    @Test
    void successfulAuthentication_writesCookieAndBody() throws IOException {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager, jwtUtils, bearerTokenService);

        UserModel user = new UserModel();
        user.setId(1);
        user.setUsername("alice");
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setIsActive(true);
        user.setIsLocked(false);
        CompanyModel company = new CompanyModel();
        company.setId(10);
        company.setName("ACME");
        user.setCompanyModel(company);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("USER")));

        JwtDto jwtDto = JwtDto.builder()
                .userModel(user)
                .accessToken("access-token-123")
                .build();
        when(jwtUtils.generateTokensForUser(user)).thenReturn(jwtDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.successfulAuthentication(request, response, mock(FilterChain.class), auth);

        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        // Verify cookie and body
        assertTrue(response.getCookies().length > 0);
        String body = response.getContentAsString();
        assertTrue(body.contains("access-token-123"));
        assertTrue(body.contains("alice"));
    }
}
