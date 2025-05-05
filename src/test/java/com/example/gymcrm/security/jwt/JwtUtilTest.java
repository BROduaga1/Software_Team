package com.example.gymcrm.security.jwt;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Mock
    private JwtEncoder encoder;

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpirationSec", 900L);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpirationSec", 18000L);
    }

    @Test
    void generateToken_WithMultipleAuthorities_ShouldCreateToken() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("test-token-value");
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = jwtUtil.generateToken(authentication);

        assertNotNull(token);
        assertEquals("test-token-value", token);

        verify(authentication).getName();
        verify(authentication).getAuthorities();
        verify(encoder).encode(any(JwtEncoderParameters.class));
    }


    @Test
    void generateToken_WithNoAuthorities_ShouldCreateToken() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("test-token-value");
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = jwtUtil.generateToken(authentication);

        assertNotNull(token);
        assertEquals("test-token-value", token);

        verify(authentication).getName();
        verify(authentication).getAuthorities();
        verify(encoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void generateRefreshToken_ShouldCreateTokenWithRefreshType() {
        String username = "testuser";

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("test-token-value");
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = jwtUtil.generateRefreshToken(username);

        assertNotNull(token);
        assertEquals("test-token-value", token);

        verify(encoder).encode(any(JwtEncoderParameters.class));
    }
}
