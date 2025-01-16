package exambyte.presentation.controllers.securityHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

/**
 * Diese Klasse ist eine benutzerdefinierte Factory, die den SecurityContext für Tests mit einer simulierten
 * OAuth2-Benutzeridentität erstellt. Sie wird in Kombination mit der Annotation {@link WithMockOAuth2User} verwendet,
 * um in Tests eine gefälschte OAuth2-Benutzersitzung zu simulieren.
 *
 * Die Methode createSecurityContext erstellt den SecurityContext basierend auf den Attributen der {@link WithMockOAuth2User}-Annotation.
 * Dabei werden die angegebenen Rollen und Berechtigungen (authorities) in eine List von GrantedAuthority umgewandelt.
 * Wenn keine Berechtigungen angegeben sind, werden die Rollen zu Berechtigungen umgewandelt, und es wird sichergestellt,
 * dass die Rollen nicht mit "ROLE_" beginnen.
 *
 * Mögliche Fehler:
 * - Wenn sowohl Rollen als auch Berechtigungen angegeben sind, wird eine {@link IllegalStateException} geworfen, es sei denn,
 *   die Rollen sind auf "USER" gesetzt.
 *
 * Der resultierende SecurityContext enthält ein {@link OAuth2AuthenticationToken}, das die Information des Benutzers
 * sowie die Berechtigungen und die Client-Registrierung enthält.
 */
public class WithOAuth2UserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockOAuth2User> {

    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2User withUser) {
        // Berechtigungen für den Benutzer aufbauen
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : withUser.authorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        // Rollen werden als Berechtigungen hinzugefügt, wenn keine spezifischen Berechtigungen angegeben sind
        if (grantedAuthorities.isEmpty()) {
            for (String role : withUser.roles()) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        // Ausnahme, wenn sowohl Rollen als auch Berechtigungen definiert sind
        else if (!(withUser.roles().length == 1 && "USER".equals(withUser.roles()[0]))) {
            throw new IllegalStateException(
                    "You cannot define roles attribute " + Arrays.asList(withUser.roles())
                            + " with authorities attribute " + Arrays.asList(withUser.authorities()));
        }

        // OAuth2User mit den festgelegten Berechtigungen und Benutzerdaten erstellen
        OAuth2User principal = new DefaultOAuth2User(grantedAuthorities,
                Map.of("id", withUser.id(), "login", withUser.login()), "id");

        // Authentifizierungsobjekt erstellen
        Authentication auth =
                new OAuth2AuthenticationToken(principal, principal.getAuthorities(),
                        withUser.clientRegistrationId());

        // SecurityContext erstellen und Authentifizierung setzen
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
