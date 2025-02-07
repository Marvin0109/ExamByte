package exambyte.infrastructure.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AppUserService {
    OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest);
}