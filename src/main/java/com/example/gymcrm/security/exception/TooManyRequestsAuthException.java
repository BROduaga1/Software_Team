package com.example.gymcrm.security.exception;

import org.springframework.security.core.AuthenticationException;

public class TooManyRequestsAuthException extends AuthenticationException {
    public TooManyRequestsAuthException(String msg) {
        super(msg);
    }
}
