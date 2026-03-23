package exambyte.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * This configuration class enables method-level security in the application.
 *
 * <p>With the {@link EnableMethodSecurity} annotation, it becomes possible to define security constraints
 * at method level, such as using
 * {@link org.springframework.security.access.annotation.Secured}
 * or {@link org.springframework.security.access.prepost.PreAuthorize} for access control.</p>
 *
 * @see EnableMethodSecurity
 */

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {
}
