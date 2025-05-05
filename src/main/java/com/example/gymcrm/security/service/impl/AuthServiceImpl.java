package com.example.gymcrm.security.service.impl;

import com.example.gymcrm.domain.User;
import com.example.gymcrm.dto.user.ChangeLoginDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.security.dto.JwtDto;
import com.example.gymcrm.security.dto.RefreshTokenDto;
import com.example.gymcrm.security.jwt.JwtUtil;
import com.example.gymcrm.security.service.AuthService;
import com.example.gymcrm.util.TransactionIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private static final String USER_NOT_FOUND_MESSAGE_TEMPLATE = "User not found with username: {}";
    private final AuthenticationManager authenticationManager;
    private final TransactionIdGenerator transactionIdGenerator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void changePassword(ChangeLoginDto changeLoginDto) {
        User user = userRepository.findByUsername(changeLoginDto.getUsername())
                .orElseThrow(() -> {
                    LOGGER.info(USER_NOT_FOUND_MESSAGE_TEMPLATE, changeLoginDto.getUsername());
                    return new EntityNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(changeLoginDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(changeLoginDto.getNewPassword()));
        userRepository.save(user);
        LOGGER.info("Password changed successfully for user: {}", changeLoginDto.getUsername());
    }

    @Override
    @Transactional
    public JwtDto authenticate(UserLoginDto request) {
        String transactionId = transactionIdGenerator.generateTransactionId();
        LOGGER.info("Authenticating user: {} with transaction ID: {}", request.getUsername(), transactionId);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String accessToken = jwtUtil.generateToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public JwtDto refreshAccessToken(RefreshTokenDto request) {
        try {
            var claims = jwtDecoder.decode(request.getRefreshToken()).getClaims();
            if (!"refresh".equals(claims.get("token_type"))) {
                throw new IllegalArgumentException("Invalid token type");
            }

            String username = claims.get("sub").toString();
            if (!userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("User not found");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);
            String accessToken = jwtUtil.generateToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            return JwtDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            LOGGER.error("Error validating refresh token", e);
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
    }

    @Override
    public void logout(RefreshTokenDto request) {
        LOGGER.info("User logged out - client should clear tokens");
    }
}
