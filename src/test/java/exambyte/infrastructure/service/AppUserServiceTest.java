package exambyte.infrastructure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    private static UserCreationService userCreationService;
    private static AppUserServiceImpl appUserService;
    private OAuth2User user;

    @BeforeEach
    void setUp() {
        userCreationService = mock(UserCreationService.class);
        appUserService = new AppUserServiceImpl(userCreationService);
        user = createOAuth2User();
    }


    private OAuth2User createOAuth2User() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "");
        attributes.put("login", "Marvin0109");
        attributes.put("name", "dummyName");

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return attributes;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public String getName() {
                return (String) attributes.get("name");
            }
        };

    }

    @Test
    @DisplayName("Ein schon vorhandener Professor erhält die Admin Rolle")
    void test_01() {
        // Arrange
        String login = user.getAttributes().get("login").toString();

        when(userCreationService.checkProfessor(login)).thenReturn(true);
        when(userCreationService.checkKorrektor(login)).thenReturn(false);
        when(userCreationService.checkStudent(login)).thenReturn(false);

        // Act
        OAuth2User result = appUserService.addRole(user);

        // Assert
        assertEquals("Marvin0109", login);
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_ADMIN");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_USER");

        verify(userCreationService).checkProfessor(login);
        verify(userCreationService).checkKorrektor(login);
        verify(userCreationService).checkStudent(login);
    }

    @Test
    @DisplayName("Ein schon vorhandener Korrektor erhält die Reviewer Rolle")
    void test_02() {
        // Arrange
        String login = user.getAttributes().get("login").toString();

        when(userCreationService.checkProfessor(login)).thenReturn(false);
        when(userCreationService.checkKorrektor(login)).thenReturn(true);
        when(userCreationService.checkStudent(login)).thenReturn(false);

        // Act
        OAuth2User result = appUserService.addRole(user);

        // Assert
        assertEquals("Marvin0109", login);
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_REVIEWER");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_USER");

        verify(userCreationService).checkKorrektor(login);
        verify(userCreationService).checkProfessor(login);
        verify(userCreationService).checkStudent(login);
    }

    @Test
    @DisplayName("Ein schon vorhandener Student erhält die Student Rolle")
    void test_03() {
        // Arrange
        String login = user.getAttributes().get("login").toString();

        when(userCreationService.checkProfessor(login)).thenReturn(false);
        when(userCreationService.checkKorrektor(login)).thenReturn(false);
        when(userCreationService.checkStudent(login)).thenReturn(true);

        // Act
        OAuth2User result = appUserService.addRole(user);

        // Asser
        assertEquals("Marvin0109", login);
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_STUDENT");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_USER");

        verify(userCreationService).checkKorrektor(login);
        verify(userCreationService).checkProfessor(login);
        verify(userCreationService).checkStudent(login);
    }

    @Test
    @DisplayName("Ein schon vorhandener User mit allen Rechten erhält alle Rollen")
    void test_04() {
        // Arrange
        String login = user.getAttributes().get("login").toString();

        when(userCreationService.checkProfessor(login)).thenReturn(true);
        when(userCreationService.checkKorrektor(login)).thenReturn(true);
        when(userCreationService.checkStudent(login)).thenReturn(true);

        // Act
        OAuth2User result = appUserService.addRole(user);

        // Assert
        assertEquals("Marvin0109", login);
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_ADMIN");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_REVIEWER");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_STUDENT");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_USER");

        verify(userCreationService).checkKorrektor(login);
        verify(userCreationService).checkProfessor(login);
        verify(userCreationService).checkStudent(login);
    }

    // Test anpassbar
    @Test
    @DisplayName("Ein nicht gefundener User 'Marvin0109' wird alle Rechte kriegen")
    void test_05() {
        // Arrange
        String login = user.getAttributes().get("login").toString();

        when(userCreationService.checkProfessor(login)).thenReturn(false);
        when(userCreationService.checkKorrektor(login)).thenReturn(false);
        when(userCreationService.checkStudent(login)).thenReturn(false);

        // Act
        OAuth2User result = appUserService.addRole(user);
        Set<GrantedAuthority> authorities = new HashSet<>(result.getAuthorities());

        // Assert
        assertEquals("Marvin0109", login);
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_ADMIN");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_REVIEWER");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_STUDENT");
        assertThat(result.getAuthorities().stream().map(Object::toString)).contains("ROLE_USER");

        verify(userCreationService).createUser(user, authorities);
    }
}
