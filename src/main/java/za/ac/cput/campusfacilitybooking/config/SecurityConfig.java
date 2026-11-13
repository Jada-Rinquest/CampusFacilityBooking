
package za.ac.cput.campusfacilitybooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import za.ac.cput.campusfacilitybooking.security.CampusAuthenticationProvider;

/*
 * Security for the Campus Facility Booking API.
 *
 * - Sign in with POST /auth/login.
 * - Authentication is stored in the HTTP session.
 * - PasswordEncoder is defined in PasswordConfig.java.
 * - Access depends on STUDENT, LECTURER or ADMIN roles.
 * - Unauthenticated requests return 401.
 * - Forbidden requests return 403.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] STAFF = {"LECTURER", "ADMIN"};

    @Bean
    public AuthenticationManager authenticationManager(
            CampusAuthenticationProvider provider) {
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository)
            throws Exception {

        http
                .cors(Customizer.withDefaults())

                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .securityContext(context -> context
                        .securityContextRepository(
                                securityContextRepository))

                .sessionManagement(session -> session
                        .sessionFixation(fixation ->
                                fixation.changeSessionId()))

                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED)))

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(
                                new HttpStatusReturningLogoutSuccessHandler(
                                        HttpStatus.OK)))

                .authorizeHttpRequests(auth -> auth

                        // Public pages and static resources
                        .requestMatchers(
                                "/",
                                "/*.html",
                                "/*.css",
                                "/*.js",
                                "/images/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()

                        // Public authentication endpoints
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/login",
                                "/auth/register"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/login/exists/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // Signed-in users
                        .requestMatchers(
                                HttpMethod.GET,
                                "/auth/me"
                        ).authenticated()

                        // Admin only
                        .requestMatchers(
                                "/login/**",
                                "/register/**",
                                "/userrole/**"
                        ).hasRole("ADMIN")

                        // Staff-only records
                        .requestMatchers(
                                HttpMethod.GET,
                                "/invoice/**",
                                "/maintenancerequest/**"
                        ).hasAnyRole(STAFF)

                        // Other GET requests require login
                        .requestMatchers(
                                HttpMethod.GET,
                                "/**"
                        ).authenticated()

                        // Student and staff operations
                        .requestMatchers(
                                HttpMethod.POST,
                                "/booking/create"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/booking/delete/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/notification/mark-read/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/notification/delete/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/user/update"
                        ).authenticated()

                        // Remaining changes require staff role
                        .anyRequest().hasAnyRole(STAFF)
                );

        return http.build();
    }
}
