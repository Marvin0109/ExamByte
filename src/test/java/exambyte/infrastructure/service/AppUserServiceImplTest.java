package exambyte.infrastructure.service;

import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Disabled //WORK IN PROGRESS
@RunWith(PowerMockRunner.class)
@PrepareForTest({AppUserServiceImpl.class, DefaultOAuth2UserService.class})
public class AppUserServiceImplTest {

    @Mock
    private UserCreationService userCreationService;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private OAuth2UserRequest userRequest;
    private OAuth2User originalUser;

    @Before
    public void setUp() throws Exception {
        // Mock the DefaultOAuth2UserService
        PowerMockito.mockStatic(DefaultOAuth2UserService.class);
        DefaultOAuth2UserService mockUserService = PowerMockito.mock(DefaultOAuth2UserService.class);

        // Create a mock OAuth2UserRequest
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
                .clientId("clientId")
                .clientSecret("clientSecret")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .build();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "mockTokenValue", Instant.now(), Instant.now().plusSeconds(3600));
        userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        // Create a mock OAuth2User
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("login", "Marvin0109");
        originalUser = new DefaultOAuth2User(Collections.emptyList(), attributes, "login");

        // Mock the behavior of DefaultOAuth2UserService
        when(mockUserService.loadUser(any(OAuth2UserRequest.class))).thenReturn(originalUser);
        PowerMockito.whenNew(DefaultOAuth2UserService.class).withNoArguments().thenReturn(mockUserService);
    }

    @Test
    @DisplayName("Benutzer mit Login 'Marvin0109' erhält alle ROLLEN")
    public void testUserWithAllRoles() {
        // Arrange
        Map<String, Object> mutableAttributes = new HashMap<>(originalUser.getAttributes());
        mutableAttributes.put("login", "Marvin0109");
        OAuth2User mutableUser = new DefaultOAuth2User(Set.of(new OAuth2UserAuthority(mutableAttributes)), mutableAttributes, "login");

        when(userCreationService.checkProfessor("Marvin0109")).thenReturn(true);
        when(userCreationService.checkKorrektor("Marvin0109")).thenReturn(true);
        when(userCreationService.checkStudent("Marvin0109")).thenReturn(true);

        // Act
        OAuth2User user = appUserService.loadUser(userRequest);

        // Assert
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_REVIEWER")));
    }

    @Test
    @DisplayName("Benutzer mit Login 'Marvin0109' erhält Rolle 'ROLE_ADMIN'")
    public void testUserWithLoginMarvin0109() {
        // Arrange
        Map<String, Object> mutableAttributes = new HashMap<>(originalUser.getAttributes());
        mutableAttributes.put("login", "Marvin0109");
        OAuth2User mutableUser = new DefaultOAuth2User(Set.of(new OAuth2UserAuthority(mutableAttributes)), mutableAttributes, "login");

        when(userCreationService.checkProfessor("Marvin0109")).thenReturn(false);
        when(userCreationService.checkKorrektor("Marvin0109")).thenReturn(false);
        when(userCreationService.checkStudent("Marvin0109")).thenReturn(false);

        // Act
        OAuth2User user = appUserService.loadUser(userRequest);

        // Assert
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(userCreationService).createUser(eq(mutableUser), anySet());
    }

    @Test
    @DisplayName("Benutzer erhält Rolle 'ROLE_REVIEWER', wenn checkKorrektor true ist")
    public void testUserWithReviewerRole() {
        // Arrange
        Map<String, Object> mutableAttributes = new HashMap<>(originalUser.getAttributes());
        mutableAttributes.put("login", "reviewerUser");
        OAuth2User mutableUser = new DefaultOAuth2User(Set.of(new OAuth2UserAuthority(mutableAttributes)), mutableAttributes, "login");

        when(userCreationService.checkProfessor("reviewerUser")).thenReturn(false);
        when(userCreationService.checkKorrektor("reviewerUser")).thenReturn(true);
        when(userCreationService.checkStudent("reviewerUser")).thenReturn(false);

        // Act
        OAuth2User user = appUserService.loadUser(userRequest);

        // Assert
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_REVIEWER")));
        verify(userCreationService, never()).createUser(eq(mutableUser), anySet());
    }

    @Test
    @DisplayName("Benutzer erhält Rolle 'ROLE_STUDENT', wenn checkStudent true ist")
    public void testUserWithStudentRole() {
        // Arrange
        Map<String, Object> mutableAttributes = new HashMap<>(originalUser.getAttributes());
        mutableAttributes.put("login", "studentUser");
        OAuth2User mutableUser = new DefaultOAuth2User(Set.of(new OAuth2UserAuthority(mutableAttributes)), mutableAttributes, "login");

        when(userCreationService.checkProfessor("studentUser")).thenReturn(false);
        when(userCreationService.checkKorrektor("studentUser")).thenReturn(false);
        when(userCreationService.checkStudent("studentUser")).thenReturn(true);

        // Act
        OAuth2User user = appUserService.loadUser(userRequest);

        // Assert
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        verify(userCreationService, never()).createUser(eq(mutableUser), anySet());
    }

    @Test
    @DisplayName("Neuer Benutzer mit unbekanntem Login erhält die entsprechenden Rollen und wird erstellt")
    public void testNewUserCreation() {
        // Arrange
        Map<String, Object> mutableAttributes = new HashMap<>(originalUser.getAttributes());
        mutableAttributes.put("login", "newUser");
        OAuth2User mutableUser = new DefaultOAuth2User(Set.of(new OAuth2UserAuthority(mutableAttributes)), mutableAttributes, "login");

        when(userCreationService.checkProfessor("newUser")).thenReturn(false);
        when(userCreationService.checkKorrektor("newUser")).thenReturn(false);
        when(userCreationService.checkStudent("newUser")).thenReturn(false);

        // Act
        OAuth2User user = appUserService.loadUser(userRequest);

        // Assert
        assertTrue(user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        verify(userCreationService).createUser(eq(mutableUser), anySet());
    }

    // Helper method to create a mock ClientRegistration
    private ClientRegistration createMockClientRegistration() {
        return ClientRegistration.withRegistrationId("github")
                .clientId("clientId")
                .clientSecret("clientSecret")
                .scope("read:user")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .clientName("GitHub")
                .build();
    }
}