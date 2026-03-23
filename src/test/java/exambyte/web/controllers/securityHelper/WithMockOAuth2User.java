package exambyte.web.controllers.securityHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * This custom annotation is used to create a mock OAuth2 user for tests.
 * It can be applied to methods and classes and allows simulated OAuth2 user data to be provided.
 * The annotation is combined with a custom factory {@link WithOAuth2UserSecurityContextFactory}
 * to set up the SecurityContext for tests.
 *
 * <p>Possible attributes:</p>
 * <ul>
 *     <li>id: The user ID (default: 666666).</li>
 *     <li>login: The username of the mock OAuth2 user (default: "username").</li>
 *     <li>roles: The roles of the user (default: "USER").</li>
 *     <li>authorities: Additional permissions for the user.</li>
 *     <li>clientRegistrationId: The client registration ID to be used (default: "github").</li>
 * </ul>
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithOAuth2UserSecurityContextFactory.class)
public @interface WithMockOAuth2User {
    int id() default 666666;

    String login() default "username";

    String[] roles() default {"USER"};

    String[] authorities() default {};

    String clientRegistrationId() default "github";
}
