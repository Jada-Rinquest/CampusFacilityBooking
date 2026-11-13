package za.ac.cput.campusfacilitybooking.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import za.ac.cput.campusfacilitybooking.dto.AuthResponse;
import za.ac.cput.campusfacilitybooking.service.LoginService;

/*
 * Plugs the domain's LoginService into Spring Security.
 *
 * LoginService checks the BCrypt hash and works out the role
 * (Login -> Register -> User -> UserRole); this class turns the
 * result into an Authentication with a ROLE_STUDENT, ROLE_LECTURER
 * or ROLE_ADMIN authority, which the URL rules and @PreAuthorize use.
 */
@Component
public class CampusAuthenticationProvider implements AuthenticationProvider {

    private final LoginService loginService;

    public CampusAuthenticationProvider(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        String password = credentials == null ? null : credentials.toString();

        AuthResponse details = loginService.authenticate(username, password);
        if (details == null) {
            // Same message whether the username or the password was wrong
            throw new BadCredentialsException("Invalid username or password.");
        }

        AuthenticatedUser principal = new AuthenticatedUser(details);
        return UsernamePasswordAuthenticationToken.authenticated(
                principal,
                null, // never keep the raw password in the session
                List.of(new SimpleGrantedAuthority("ROLE_" + details.getRole()))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
