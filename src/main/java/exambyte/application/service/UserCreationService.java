package exambyte.application.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Set;

public interface UserCreationService {
    boolean checkStudent(String username);

    boolean checkKorrektor(String username);

    boolean checkProfessor(String username);

    void createUser(OAuth2User user, Set<GrantedAuthority> authorities);
}
