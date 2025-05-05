package com.example.gymcrm.controller;

import com.example.gymcrm.controller.logging.LogTransaction;
import com.example.gymcrm.dto.user.ChangeLoginDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.security.dto.JwtDto;
import com.example.gymcrm.security.dto.RefreshTokenDto;
import com.example.gymcrm.security.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Auth", description = "user authentication operations")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtDto authenticate(@RequestBody @Validated UserLoginDto request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtDto refreshAccessToken(@RequestBody @Validated RefreshTokenDto request) {
        return authService.refreshAccessToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestBody @Validated RefreshTokenDto request) {
        authService.logout(request);
        return ResponseEntity.ok("User successfully logged out");
    }

    @LogTransaction
    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            @RequestBody @Validated ChangeLoginDto changeLoginDto
    ) {
        authService.changePassword(changeLoginDto);
    }
}
