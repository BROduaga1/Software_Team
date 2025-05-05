package com.example.gymcrm.security.service;

import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.security.dto.JwtDto;
import com.example.gymcrm.security.dto.RefreshTokenDto;
import com.example.gymcrm.security.jwt.JwtUtil;
import com.example.gymcrm.security.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void refreshAccessToken_InvalidTokenType_ShouldThrowException() {
        RefreshTokenDto request = new RefreshTokenDto();
        request.setRefreshToken("invalid-token");

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("token_type", "access", "sub", "username"));
        when(jwtDecoder.decode(request.getRefreshToken())).thenReturn(jwt);

        assertThrows(IllegalArgumentException.class, () -> authService.refreshAccessToken(request));
    }

    @Test
    void refreshAccessToken_UserNotFound_ShouldThrowException() {
        RefreshTokenDto request = new RefreshTokenDto();
        request.setRefreshToken("valid-token");

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("token_type", "refresh", "sub", "nonexistentUser"));
        when(jwtDecoder.decode(request.getRefreshToken())).thenReturn(jwt);
        when(userRepository.existsByUsername("nonexistentUser")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.refreshAccessToken(request));
    }

    @Test
    void refreshAccessToken_ValidToken_ShouldReturnNewTokens() {
        RefreshTokenDto request = new RefreshTokenDto();
        request.setRefreshToken("valid-token");

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("token_type", "refresh", "sub", "existingUser"));
        when(jwtDecoder.decode(request.getRefreshToken())).thenReturn(jwt);
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("new-access-token");
        when(jwtUtil.generateRefreshToken("existingUser")).thenReturn("new-refresh-token");

        JwtDto jwtDto = authService.refreshAccessToken(request);

        assertNotNull(jwtDto);
        assertEquals("new-access-token", jwtDto.getAccessToken());
        assertEquals("new-refresh-token", jwtDto.getRefreshToken());
    }
}
