package exambyte.infrastructure.service;

import exambyte.domain.config.AppUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * UserService implementiert die {@link OAuth2UserService} Schnittstelle und wird verwendet,
 * um Benutzerinformationen von einem OAuth2-Provider zu laden und die zugehörigen Benutzerrollen zu setzen.
 *
 * Diese Klasse überprüft den "login"-Wert des Benutzers und weist dem Benutzer basierend auf den Wert
 * eine bestimmte Rolle zu. In diesem Fall wird der Benutzer mit dem Login "Marvin0109" die Rolle "ROLE_ADMIN"
 * zugewiesen.
 *
 * @see OAuth2UserService
 * @see DefaultOAuth2UserService
 */
@Service
public class AppUserServiceImpl implements AppUserService {

  private final UserCreationService userCreationService;

  public AppUserServiceImpl(UserCreationService userCreationService) {
    this.userCreationService = userCreationService;
  }

  /**
   * Lädt die Benutzerinformationen vom OAuth2-Provider und weist dem Benutzer, basierend auf seinem Login,
   * die entsprechende Rolle zu.
   *
   * Wenn der Benutzer den Login "Marvin0109" hat, wird ihm die Rolle "ROLE_ADMIN" zugewiesen.
   *
   * @param userRequest Die Anfrage, die Benutzerdaten vom OAuth2-Provider anfordert.
   * @return Ein {@link OAuth2User} Objekt, das die Benutzerinformationen und die zugewiesenen Rollen enthält.
   * @throws OAuth2AuthenticationException Wenn ein Fehler beim Laden der Benutzerdaten auftritt.
   */
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("User Service called");
    OAuth2User originalUser = new DefaultOAuth2UserService().loadUser(userRequest);
    Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());
    String login = originalUser.getAttribute("login");

    // Es wird erstmal überprüft, ob schon der User in der DB gespeichert worden ist, wenn ja,
    // kriegt er nur die Rolle
    if (userCreationService.checkProfessor(login)) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else if (userCreationService.checkKorrektor(login)) {
      authorities.add(new SimpleGrantedAuthority("ROLE_REVIEWER"));
    } else if (userCreationService.checkStudent(login)) {
      authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
    } else {
      // Wenn der Benutzer noch nicht existiert, erstell ihn mit einer passenden Rolle
      if ("Marvin0109".equals(login) || "muz70wuc".equals(login)) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      } else if ("KorrektorName".equals(login)) {
        authorities.add(new SimpleGrantedAuthority("ROLE_REVIEWER"));
      } else {
        authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
      }

      // Neuen Benutzer in die DB speichern
      userCreationService.createUser(originalUser, authorities);
    }

    return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "id");
  }
}
