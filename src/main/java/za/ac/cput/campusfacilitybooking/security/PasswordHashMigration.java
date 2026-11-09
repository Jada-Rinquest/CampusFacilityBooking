package za.ac.cput.campusfacilitybooking.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import za.ac.cput.campusfacilitybooking.service.LoginService;

/*
 * Runs once at startup, after data.sql has loaded.
 *
 * Accounts created before password hashing was added (and the demo
 * accounts in data.sql) have plain-text passwords. This replaces each
 * one with its BCrypt hash, so the same passwords keep working.
 */
@Component
public class PasswordHashMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PasswordHashMigration.class);

    private final LoginService loginService;

    public PasswordHashMigration(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void run(ApplicationArguments args) {
        int upgraded = loginService.upgradePlaintextPasswords();
        if (upgraded > 0) {
            log.info("Hashed {} plain-text password(s) with BCrypt.", upgraded);
        }
    }
}
