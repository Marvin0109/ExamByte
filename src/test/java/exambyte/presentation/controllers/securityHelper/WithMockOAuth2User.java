package exambyte.presentation.controllers.securityHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * Diese benutzerdefinierte Annotation wird verwendet, um ein Mock-OAuth2-Benutzerobjekt für Tests zu erstellen.
 * Sie kann auf Methoden und Klassen angewendet werden und ermöglicht es, simuliert OAuth2-Benutzerdaten bereitzustellen.
 * Die Annotation wird mit einer benutzerdefinierten Factory {@link WithOAuth2UserSecurityContextFactory} kombiniert,
 * um den SecurityContext für die Tests zu setzen.
 *
 * Mögliche Attribute:
 * - id: Die ID des Benutzers (Standard: 666666).
 * - login: Der Benutzername des Mock-OAuth2-Benutzers (Standard: "username").
 * - roles: Die Rollen des Benutzers (Standard: "USER").
 * - authorities: Zusätzliche Berechtigungen für den Benutzer.
 * - clientRegistrationId: Die Client-Registrierung-ID, die verwendet werden soll (Standard: "github").
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
