package exambyte.application.config;

import exambyte.application.service.AppUserServiceImpl;
import exambyte.application.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Die Klasse konfiguriert die Sicherheitslinien für die Webanwendung, einschließlich
 * der Authentifizierung und der Autorisierung von Benutzern sowie der Logout-Logik.
 *
 * - Die Konfiguration legt fest, welche URLs öffentlich zugänglich sind und welche eine Authentifizierung erfordern.
 * - Eine OAuth2-Login-Integration wird konfiguriert, bei der ein benutzerdefinierter {@link AppUserServiceImpl} verwendet wird,
 *   um Benutzerinformationen zu laden.
 * - Die Logout-Logik wird so konfiguriert, dass nach dem Logout die Session invalidiert und bestimmte Cookies gelöscht werden.
 *
 * Hinweis: Es gibt ein bekanntes Problem mit der Logout-Funktionalität, bei dem der Browser geschlossen werden muss,
 * um den Login-Cookie zu löschen.
 *
 * @see AppUserServiceImpl
 */

@Configuration
public class SecurityConfig {

    /**
     * Konstruktor für die {@link SecurityConfig}, der den {@link AppUserServiceImpl} injiziert.
     *
     * @param userService Der benutzerdefinierte {@link AppUserServiceImpl}, der zur Authentifizierung der Benutzer verwendet wird.
     */

    private final AppUserService appUserService;

    @Autowired
    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Die Methode konfiguriert die Sicherheitsfilterkette, um den Zugriff auf verschiedene URLs und die Authentifizierung zu regeln.
     *
     * - Bestimmte URLs (wie z.B. "/login" und "/public/**") sind ohne Authentifizierung zugänglich
     * - Alle anderen Anfragen erfordern eine Authentifizierung.
     * - Die Oauth2-Login-Integration wird konfiguriert, um den {@link AppUserServiceImpl} zu verwenden.
     * - Die Logout-Logik wird konfiguriert, um die Sitzung zu invalidieren und Cookies zu löschen.
     *
     * @param chainBuilder Der {@link HttpSecurity}-Builder, der für die Sicherheitskonfiguration verwendet wird.
     * @return Eine konfigurierte {@link SecurityFilterChain}-Instanz, die die Sicherheitsrichtlinien anwendet.
     * @throws Exception Wenn ein Fehler bei der Konfiguration der Sicherheitsfilterkette auftritt.
     */
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
