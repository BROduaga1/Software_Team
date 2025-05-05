package com.example.gymcrm.security.brute_force;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    private static final Integer maxAttempt = 3;
    @Mock
    private HttpServletRequest request;
    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptService(request, maxAttempt, 10);
    }

    @Test
    void testIsBlockedWhenAttemptsExceeded(){
        when(request.getHeader("X-Forwarded-For")).thenReturn("0.0.0.0");
        String clientIP = "0.0.0.0";

        for (int i = 0; i <= maxAttempt; i++) {
            loginAttemptService.loginFailed(clientIP);
        }

        assertTrue(loginAttemptService.isBlocked(), "User should be blocked after max attempts");
    }

    @Test
    void testIsNotBlockedWhenAttemptsBelowMax() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("0.0.0.2");
        String clientIP = "0.0.0.2";

        for (int i = 0; i < maxAttempt - 1; i++) {
            loginAttemptService.loginFailed(clientIP);
        }

        assertFalse(loginAttemptService.isBlocked(), "User should not be blocked before reaching max attempts");
    }

    @Test
    void testLoginFailedExecutionException() throws Exception {
        String clientIP = "0.0.0.3";

        CacheLoader<String, AtomicInteger> cacheLoader = new CacheLoader<>() {
            @Override
            public @NotNull AtomicInteger load(@NotNull String key) throws ExecutionException {
                throw new ExecutionException(new Exception("Simulated exception"));
            }
        };
        LoadingCache<String, AtomicInteger> customCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(cacheLoader);

        setPrivateField(loginAttemptService, customCache);

        loginAttemptService.loginFailed(clientIP);
        assertNull(customCache.getIfPresent(clientIP), "The attemptsCache should not contain the clientIP key");
    }

    @Test
    void testIsBlockedExecutionException() throws Exception {
        when(request.getHeader("X-Forwarded-For")).thenReturn("0.0.0.4");
        CacheLoader<String, AtomicInteger> cacheLoader = new CacheLoader<>() {
            @Override
            public @NotNull AtomicInteger load(@NotNull String key) throws ExecutionException {
                throw new ExecutionException(new Exception("Simulated exception"));
            }
        };
        LoadingCache<String, AtomicInteger> customCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .build(cacheLoader);

        setPrivateField(loginAttemptService, customCache);

        assertFalse(loginAttemptService.isBlocked(), "User should not be blocked when ExecutionException occurs");
    }

    @Test
    void testGetClientIPWithoutXForwardedForHeader() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        String clientIP = loginAttemptService.getClientIP();

        assertEquals("192.168.1.1", clientIP, "The client IP should be the remote address when X-Forwarded-For header is not present");
    }

    private void setPrivateField(Object target, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField("attemptsCache");
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testCacheLoaderWithNullKey() throws Exception {
        Class<?> defaultCacheLoaderClass = Class.forName("com.example.gymcrm.security.brute_force.LoginAttemptService$DefaultCacheLoader");
        Constructor<?> constructor = defaultCacheLoaderClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        CacheLoader<String, AtomicInteger> cacheLoader = (CacheLoader<String, AtomicInteger>) constructor.newInstance();
        assertThrows(NullPointerException.class, () -> cacheLoader.load(null), "Expected load() to throw, but it didn't");
    }

    @Test
    void testLoginFailedWithNullKey() {
        assertThrows(NullPointerException.class, () -> loginAttemptService.loginFailed(null), "Expected loginFailed() to throw, but it didn't");
    }
}
