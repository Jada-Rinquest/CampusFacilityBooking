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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import za.ac.cput.campusfacilitybooking.security.CampusAuthenticationProvider;

/*
 * Security for the Campus Facility Booking API.
 *
 *  - Sign in with POST /auth/login. The server keeps the signed-in user in
 *    the HTTP session (JSESSIONID cookie: HttpOnly, SameSite=Strict).
 *  - Passwords are stored as BCrypt hashes.
 *  - Access depends on the role: STUDENT, LECTURER or ADMIN
 *    (see authorizeHttpRequests below and @PreAuthorize on controllers).
 *  - Unauthenticated API calls get 401, forbidden ones get 403.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] STAFF = {"LECTURER", "ADMIN"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CampusAuthenticationProvider provider) {
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityContextRepository securityContextRepository)
            throws Exception {

        http
                .cors(Customizer.withDefaults())

                // The frontend calls the API with fetch() from the same origin.
                // CSRF is covered by the SameSite=Strict session cookie
                // (application.properties): browsers do not send it on
                // requests that start on another site.
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository))

                .sessionManagement(session -> session
                        .sessionFixation(fixation -> fixation.changeSessionId()))

                // API clients get a status code instead of a redirect to a login page
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))

                .authorizeHttpRequests(auth -> auth

                        // ---- Public: pages, styles, scripts, sign-in, registration ----
                        .requestMatchers("/", "/*.html", "/*.css", "/*.js",
                                "/images/**", "/favicon.ico", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/login/exists/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ---- Any signed-in user ----
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

                        // ---- Admin only: credentials, registrations and roles ----
                        .requestMatchers("/login/**", "/register/**", "/userrole/**").hasRole("ADMIN")

                        // ---- Staff only to read: financial and maintenance records ----
                        .requestMatchers(HttpMethod.GET, "/invoice/**", "/maintenancerequest/**").hasAnyRole(STAFF)

                        // ---- Reading everything else needs a sign-in ----
                        .requestMatchers(HttpMethod.GET, "/**").authenticated()

                        // ---- Students may change only their own records;
                        //      @PreAuthorize on these methods checks ownership ----
                        .requestMatchers(HttpMethod.POST, "/booking/create").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/booking/delete/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/notification/mark-read/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/notification/delete/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/update").authenticated()

                        // ---- Every other change: staff only ----
                        .anyRequest().hasAnyRole(STAFF)
                );

        return http.build();
    }
}
