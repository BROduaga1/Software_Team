package com.example.gymcrm.security.service.integration;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.security.dto.JwtDto;
import com.example.gymcrm.security.dto.RefreshTokenDto;
import com.example.gymcrm.security.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


class AuthServiceIT extends BaseH2Test {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.findByUsername("testuser").ifPresent(user -> userRepository.delete(user));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnJwtTokens() {
        UserLoginDto loginDto = TestData.getExistingAdminLoginDto();

        JwtDto jwtDto = authService.authenticate(loginDto);

        assertNotNull(jwtDto);
        assertNotNull(jwtDto.getAccessToken());
        assertNotNull(jwtDto.getRefreshToken());
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldThrowException() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("wrongPassword");

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(loginDto));
    }

    @Test
    void refreshAccessToken_WithValidRefreshToken_ShouldReturnNewTokens() {
        UserLoginDto loginDto = TestData.getExistingAdminLoginDto();
        JwtDto originalTokens = authService.authenticate(loginDto);

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken(originalTokens.getRefreshToken());

        JwtDto refreshedTokens = authService.refreshAccessToken(refreshTokenDto);

        assertNotNull(refreshedTokens);
        assertNotNull(refreshedTokens.getAccessToken());
        assertNotNull(refreshedTokens.getRefreshToken());
        assertNotEquals(originalTokens.getAccessToken(), refreshedTokens.getAccessToken());
    }

    @Test
    void refreshAccessToken_WithInvalidRefreshToken_ShouldThrowException() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken("invalid-refresh-token");
        assertThrows(IllegalArgumentException.class, () -> authService.refreshAccessToken(refreshTokenDto));
    }

    @Test
    void logout_ShouldCompleteSuccessfully() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken("any-token");
        assertDoesNotThrow(() -> authService.logout(refreshTokenDto));
    }

}
