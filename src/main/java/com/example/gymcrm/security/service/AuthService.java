package com.example.gymcrm.security.service;

import com.example.gymcrm.dto.user.ChangeLoginDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.security.dto.JwtDto;
import com.example.gymcrm.security.dto.RefreshTokenDto;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.NoSuchElementException;

/**
 * Service interface for authentication and authorization operations.
 */
public interface AuthService {

    /**
     * Changes the password of a user.
     *
     * @param loginDto the DTO containing the username, old password, and new password
     * @throws EntityNotFoundException if the user is not found
     */
    void changePassword(ChangeLoginDto loginDto) throws EntityNotFoundException;

    /**
     * Authenticates a user and generates JWT tokens.
     *
     * @param request the DTO containing the username and password
     * @return a DTO containing the access token and refresh token
     * @throws BadCredentialsException if the credentials are invalid
     */
    JwtDto authenticate(UserLoginDto request) throws BadCredentialsException;

    /**
     * Refreshes the access token using a refresh token.
     *
     * @param request the DTO containing the refresh token
     * @return a DTO containing the new access token and refresh token
     * @throws BadCredentialsException if the refresh token is invalid
     * @throws NoSuchElementException  if the user is not found
     */
    JwtDto refreshAccessToken(RefreshTokenDto request) throws BadCredentialsException, NoSuchElementException;

    /**
     * Logs out a user by invalidating the refresh token.
     *
     * @param request the DTO containing the refresh token
     */
    void logout(RefreshTokenDto request);
}
