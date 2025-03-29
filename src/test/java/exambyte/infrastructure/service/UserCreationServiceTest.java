package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.service.KorrektorService;
import exambyte.domain.service.ProfessorService;
import exambyte.domain.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserCreationServiceTest {

    private StudentService studentService;
    private KorrektorService korrektorService;
    private ProfessorService professorService;
    private UserCreationService userCreationService;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentServiceImpl.class);
        korrektorService = mock(KorrektorServiceImpl.class);
        professorService = mock(ProfessorServiceImpl.class);
        userCreationService = new UserCreationService(studentService,
                                                      korrektorService,
                                                      professorService,
                                                      mock(UserCreationService.class));
    }

    @Test
    @DisplayName("Ein geladener Student wurde gefunden")
    void test_01() {
        // Arrange
        String username = "student123";
        Student student = new Student.StudentBuilder().id(null).fachId(null).name(username).build();
        when(studentService.getStudentByName(username)).thenReturn(Optional.of(student));

        // Act
        boolean result = userCreationService.checkStudent(username);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Ein geladener Student wurde nicht gefunden")
    void test_02() {
        // Arrange
        String username = "student123";
        when(studentService.getStudentByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkStudent(username);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Ein geladener Korrektor wurde gefunden")
    void test_03() {
        // Arrange
        String username = "korrektor123";
        Korrektor korrektor = new Korrektor.KorrektorBuilder().id(null).fachId(null).name(username).build();
        when(korrektorService.getKorrektorByName(username)).thenReturn(Optional.of(korrektor));

        // Act
        boolean result = userCreationService.checkKorrektor(username);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Ein geladener Korrektor wurde nicht gefunden")
    void test_04() {
        // Arrange
        String username = "korrektor123";
        when(korrektorService.getKorrektorByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkKorrektor(username);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Ein geladener Professor wurde gefunden")
    void test_05() {
        // Arrange
        String username = "professor123";
        Professor professor = new Professor.ProfessorBuilder().id(null).fachId(null).name(username).build();
        when(professorService.getProfessorByName(username)).thenReturn(Optional.of(professor));

        // Act
        boolean result = userCreationService.checkProfessor(username);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Ein geladener Professor wurde nicht gefunden")
    void test_06() {
        // Arrange
        String username = "professor123";
        when(professorService.getProfessorByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkProfessor(username);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("User erstellen mit der Rolle Admin")
    void test_07() {
        // Arrange
        String login = "new_admin";
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        OAuth2User mockOauth2User = mock(OAuth2User.class);
        when(mockOauth2User.getAttribute("login")).thenReturn(login);

        // Act
        userCreationService.createUser(mockOauth2User, authorities);

        // Assert
        verify(professorService).saveProfessor(login);
    }

    @Test
    @DisplayName("User erstellen mit der Rolle Reviewer")
    void test_08() {
        // Arrange
        String login = "new_reviewer";
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_REVIEWER"));
        OAuth2User mockOauth2User = mock(OAuth2User.class);
        when(mockOauth2User.getAttribute("login")).thenReturn(login);

        // Act
        userCreationService.createUser(mockOauth2User, authorities);

        // Assert
        verify(korrektorService).saveKorrektor(login);
    }

    @Test
    @DisplayName("User erstellen mit der Rolle Student")
    void test_09() {
        // Arrange
        String login = "new_student";
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        OAuth2User mockOauth2User = mock(OAuth2User.class);
        when(mockOauth2User.getAttribute("login")).thenReturn(login);

        // Act
        userCreationService.createUser(mockOauth2User, authorities);

        // Assert
        verify(studentService).saveStudent(login);
    }

    // TODO: Transactional Test
}
