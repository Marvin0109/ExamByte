package exambyte.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AppUserService appUserService;

    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
        return chainBuilder
                .authorizeHttpRequests(configure -> configure
                        .requestMatchers("/", "/login", "/oauth2/**", "/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(config -> config.userInfoEndpoint(
                        info -> info.userService(appUserService)
                ))
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL für Logout
                        .logoutSuccessUrl("/") // Nach erfolgreichem Logout
                        .invalidateHttpSession(true) // Session invalidieren
                        .deleteCookies("JSESSIONID") // Cookies löschen
                        .addLogoutHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext(); // Sicherheitskontext löschen
                        })

                )
                .build();
    }
}
