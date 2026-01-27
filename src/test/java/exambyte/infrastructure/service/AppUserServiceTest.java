package exambyte.infrastructure.service;

import exambyte.application.service.AppUserService;
import exambyte.application.service.UserCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    private UserCreationService userCreationService;
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        userCreationService = mock(UserCreationService.class);
        appUserService = new AppUserServiceImpl(userCreationService);
    }

    @Test
    void addDefaultRole_createsUserIfNotFound() {
        // Arrange
        OAuth2User originalUser = mock(OAuth2User.class);
        when(originalUser.getAuthorities()).thenReturn(Set.of());
        when(originalUser.getAttribute("login")).thenReturn("testStudent");
        when(originalUser.getAttributes()).thenReturn(Map.of("id", "123"));

        when(userCreationService.checkStudent("testStudent")).thenReturn(false);

        // Act
        OAuth2User result = appUserService.addDefaultRole(originalUser);

        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));

        verify(userCreationService).createUser(eq(originalUser),
                argThat(auth -> auth.stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))));
    }

    @Test
    void addDefaultRole_doesNotCreateUserIfFound() {
        // Arrange
        OAuth2User originalUser = mock(OAuth2User.class);
        when(originalUser.getAuthorities()).thenReturn(Set.of());
        when(originalUser.getAttribute("login")).thenReturn("existingStudent");
        when(originalUser.getAttributes()).thenReturn(Map.of("id", "456"));

        when(userCreationService.checkStudent("existingStudent")).thenReturn(true);

        // Act
        OAuth2User result = appUserService.addDefaultRole(originalUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));

        verify(userCreationService, never()).createUser(any(), any());
    }
}
