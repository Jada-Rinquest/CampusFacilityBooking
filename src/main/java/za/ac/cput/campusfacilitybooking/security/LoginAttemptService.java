package za.ac.cput.campusfacilitybooking.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/*
 * Brute-force protection: after MAX_ATTEMPTS wrong passwords in a row
 * a username is locked for LOCK_TIME. A successful sign-in clears the count.
 * Kept in memory, so a server restart resets it.
 */
@Service
public class LoginAttemptService {

    static final int MAX_ATTEMPTS = 5;
    static final Duration LOCK_TIME = Duration.ofMinutes(15);

    private record Attempts(int failures, Instant lockedUntil) { }

    private final Map<String, Attempts> attempts = new ConcurrentHashMap<>();

    public boolean isLocked(String username) {
        Attempts current = attempts.get(key(username));
        if (current == null || current.lockedUntil() == null) {
            return false;
        }
        if (Instant.now().isAfter(current.lockedUntil())) {
            attempts.remove(key(username));
            return false;
        }
        return true;
    }

    public void loginFailed(String username) {
        attempts.compute(key(username), (k, current) -> {
            int failures = (current == null ? 0 : current.failures()) + 1;
            Instant lockedUntil = failures >= MAX_ATTEMPTS ? Instant.now().plus(LOCK_TIME) : null;
            return new Attempts(failures, lockedUntil);
        });
    }

    public void loginSucceeded(String username) {
        attempts.remove(key(username));
    }

    private static String key(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }
}
