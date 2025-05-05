package com.example.gymcrm.security.brute_force;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationFailureListenerTest {

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private Authentication mockAuthentication;

    @InjectMocks
    private AuthenticationFailureListener authenticationFailureListener;

    @Test
    void testOnApplicationEventWithValidXForwardedFor() {
        String clientIP = "192.168.0.1";
        String xfHeader = "192.168.0.1, 192.168.0.2";

        when(mockRequest.getHeader("X-Forwarded-For")).thenReturn(xfHeader);
        when(mockRequest.getRemoteAddr()).thenReturn(clientIP);

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        when(mockAuthentication.getName()).thenReturn("user");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
                mockAuthentication, new BadCredentialsException("Bad credentials"));

        authenticationFailureListener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("192.168.0.1");
    }

    @Test
    void testOnApplicationEventWithNoXForwardedFor() throws Exception {
        String clientIP = getClientIPUsingReflection();

        when(mockRequest.getHeader("X-Forwarded-For")).thenReturn(null);
        when(mockRequest.getRemoteAddr()).thenReturn(clientIP);

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        when(mockAuthentication.getName()).thenReturn("user");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
                mockAuthentication, new BadCredentialsException("Bad credentials"));

        authenticationFailureListener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed(clientIP);
    }

    @Test
    void testOnApplicationEventWithEmptyXForwardedFor() throws Exception {
        String clientIP = getClientIPUsingReflection();

        when(mockRequest.getHeader("X-Forwarded-For")).thenReturn("");
        when(mockRequest.getRemoteAddr()).thenReturn(clientIP);

        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        when(mockAuthentication.getName()).thenReturn("user");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
                mockAuthentication, new BadCredentialsException("Bad credentials"));

        authenticationFailureListener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed(clientIP);
    }


    private String getClientIPUsingReflection() throws Exception {
        Method getClientIPMethod = LoginAttemptService.class.getDeclaredMethod("getClientIP");
        getClientIPMethod.setAccessible(true);
        return (String) getClientIPMethod.invoke(loginAttemptService);
    }

    @Test
    void testOnApplicationEventWithNullEvent() {
        assertThrows(NullPointerException.class, () -> authenticationFailureListener.onApplicationEvent(null), "Expected onApplicationEvent() to throw, but it didn't");
    }
}
