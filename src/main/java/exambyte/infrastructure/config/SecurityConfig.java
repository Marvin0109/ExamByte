package exambyte.infrastructure.config;

import exambyte.application.service.user.AppUserService;
import exambyte.application.service.user.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class configures the security policies for the web application,
 * including authentication, authorization, and logout logic.
 *
 * <p>The configuration defines which URLs are publicly accessible and which require authentication.</p>
 *
 * <p>The OAuth2 login integration is configured using a custom
 * {@link AppUserService} to load user information.
 * The actual implementation is provided by {@link AppUserServiceImpl}.</p>
 *
 * <p>The logout logic is configured so that the session is invalidated
 * and the security context is cleared after logout.</p>
 *
 * <p><b>Important note:</b> For a new login, the browser must be closed or a new session must be started.</p>
 *
 * @see AppUserServiceImpl
 */

@Configuration
public class SecurityConfig {

    private final AppUserService appUserService;

    @Autowired
    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * This method configures the {@link SecurityFilterChain} for the web application,
     * defining access rules for URLs and controlling authentication.
     *
     * <p>Certain URLs (such as "/login" and "/public/**") are accessible without authentication.</p>
     * <p>All other requests require authentication, which is provided via OAuth2 login integration.</p>
     * <p>The logout logic ensures that the session is invalidated and cookies are cleared.</p>
     *
     * @param chainBuilder The {@link HttpSecurity} builder used to configure the security policies.
     * @return A configured {@link SecurityFilterChain} instance that applies the security rules.
     * @throws Exception If an error occurs while configuring the security filter chain.
     */

    @Bean
    public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
        return chainBuilder
                .authorizeHttpRequests(configure -> configure
                        .requestMatchers("/", "/error", "/login", "/oauth2/**", "/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(config -> config.userInfoEndpoint(
                        info -> info.userService(appUserService)
                ))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(
                                (request, response, authentication) ->
                                    SecurityContextHolder.clearContext()
                        )
                )
                .build();
    }
}