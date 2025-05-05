package com.example.gymcrm.security.brute_force;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class LoginAttemptService {

    private final LoadingCache<String, AtomicInteger> attemptsCache;
    private final HttpServletRequest request;
    private final int maxAttempts;

    public LoginAttemptService(HttpServletRequest request,
                               @Value("${login.attempt.max}") int maxAttempts,
                               @Value("${login.attempt.expire.minutes}") int expirationTimeInMinutes) {
        this.request = request;
        this.maxAttempts = maxAttempts;
        this.attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(expirationTimeInMinutes))
                .build(new DefaultCacheLoader());
    }

    public void loginFailed(final String key) {
        try {
            attemptsCache.get(key).incrementAndGet();
        } catch (final ExecutionException e) {
            LOGGER.error("Error occurred while incrementing login attempts", e);
        }
    }

    public boolean isBlocked() {
        try {
            return attemptsCache.get(getClientIP()).get() >= maxAttempts;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private static class DefaultCacheLoader extends CacheLoader<String, AtomicInteger> {
        @Override
        @NonNull
        public AtomicInteger load(@NonNull String key) {
            return new AtomicInteger(0);
        }
    }

}
