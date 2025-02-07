package exambyte.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Diese Konfigurationsklasse aktiviert die Methodensicherheit in der Anwendung.
 *
 * <p>Mit der Annotation {@link EnableMethodSecurity} wird es möglich, Sicherheitskontrollen
 * auf Methodenebene zu definieren, wie z.B. die Verwendung von {@link org.springframework.security.access.annotation.Secured}
 * oder {@link org.springframework.security.access.prepost.PreAuthorize} für den Zugriffsschutz.</p>
 *
 * @see EnableMethodSecurity
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {
}
