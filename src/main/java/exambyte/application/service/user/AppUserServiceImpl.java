package exambyte.application.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * UserService implements the {@link OAuth2UserService} interface and is used
 * to load user information from an OAuth2 provider and assign the corresponding user roles.
 * This class checks the "login" value of the user and assigns a specific role to the user.
 *
 * @see OAuth2UserService
 * @see DefaultOAuth2UserService
 */

@Service
public class AppUserServiceImpl implements AppUserService {

      private final UserCreationService userCreationService;
      Logger logger = Logger.getLogger(getClass().getName());

      public AppUserServiceImpl(UserCreationService userCreationService) {
        this.userCreationService = userCreationService;
      }

      /**
       * Loads user information from the OAuth2 provider and assigns the STUDENT role to the user.
       *
       * @param userRequest The request used to retrieve user data from the OAuth2 provider.
       * @return An {@link OAuth2User} object containing the user information and assigned roles.
       * @throws OAuth2AuthenticationException If an error occurs while loading user data.
       */

      @Override
      public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            logger.info("User Service called");
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User originalUser = delegate.loadUser(userRequest);
            return addDefaultRole(originalUser);
      }

      @Override
      public OAuth2User addDefaultRole(OAuth2User originalUser) {
            logger.info("Adding default role");
            Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());
            String login = originalUser.getAttribute("login");
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
            boolean found = userCreationService.checkStudent(login);

            if (!found) {
                  userCreationService.createUser(originalUser, authorities);
            }

            return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "id");
      }
}
