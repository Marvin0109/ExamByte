package exambyte.infrastructure.config;

import exambyte.infrastructure.service.AppUserService;
import exambyte.infrastructure.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Diese Klasse konfiguriert die Sicherheitsrichtlinien für die Webanwendung,
 * einschließlich Authentifizierung, Autorisierung und Logout-Logik.
 *
 * <p>Die Konfiguration legt fest, welche URLs öffentlich zugänglich sind und welche eine Authentifizierung erfordern.</p>
 * <p>Die OAuth2-Login-Integration wird konfiguriert, wobei ein benutzerdefinierter {@link AppUserService} verwendet wird,
 * um Benutzerinformationen zu laden. Die eigentliche Implementierung liegt in {@link AppUserServiceImpl}</p>
 * <p>Die Logout-Logik wird so konfiguriert, dass nach dem Logout die Session invalidiert und bestimmte Cookies gelöscht werden.</p>
 *
 * <p><b>Wichtiger Hinweis:</b> Es gibt ein bekanntes Problem mit der Logout-Funktionalität, bei dem der Browser geschlossen werden muss,
 * um den Login-Cookie zu löschen.</p>
 *
 * @see AppUserServiceImpl
 */
@Configuration
public class SecurityConfig {

    private final AppUserService appUserService;

    /**
     * Konstruktor für die {@link SecurityConfig}, der den {@link AppUserService} injiziert.
     *
     * @param appUserService Der benutzerdefinierte {@link AppUserService}, der zur Authentifizierung der Benutzer verwendet wird.
     */
    @Autowired
    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Diese Methode konfiguriert die {@link SecurityFilterChain} für die Webanwendung,
     * um den Zugriff auf URLs zu regeln und die Authentifizierung zu steuern.
     *
     * <p>Bestimmte URLs (wie z.B. "/login" und "/public/**") sind ohne Authentifizierung zugänglich.</p>
     * <p>Alle anderen Anfragen erfordern eine Authentifizierung, die durch eine OAuth2-Login-Integration bereitgestellt wird.</p>
     * <p>Die Logout-Logik stellt sicher, dass die Session invalidiert und Cookies gelöscht werden.</p>
     *
     * @param chainBuilder Der {@link HttpSecurity}-Builder, der verwendet wird, um die Sicherheitsrichtlinien zu konfigurieren.
     * @return Eine konfigurierte {@link SecurityFilterChain}-Instanz, die die Sicherheitsrichtlinien anwendet.
     * @throws Exception Falls ein Fehler bei der Konfiguration der Sicherheitsfilterkette auftritt.
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
                        .addLogoutHandler(
                                (request, response, authentication) ->
                            SecurityContextHolder.clearContext() // Sicherheitskontext löschen
                        )
                )
                .build();
    }
}