package za.ac.cput.campusfacilitybooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // allowCredentials must stay false: several controllers use
        // @CrossOrigin(origins = "*"), and Spring rejects "*" combined with
        // credentials, which made /login and /user return 500 errors.
        // The frontend sends no cookies, so credentials are not needed.
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}