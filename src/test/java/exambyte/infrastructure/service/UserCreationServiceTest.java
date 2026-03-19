package exambyte.infrastructure.service;

import exambyte.application.service.UserCreationService;
import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.service.ReviewerService;
import exambyte.domain.service.ProfessorService;
import exambyte.domain.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserCreationServiceTest {

    private StudentService studentService;
    private ReviewerService reviewerService;
    private ProfessorService professorService;
    private UserCreationService userCreationService;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentServiceImpl.class);
        reviewerService = mock(ReviewerServiceImpl.class);
        professorService = mock(ProfessorServiceImpl.class);
        userCreationService = new UserCreationServiceImpl(studentService,
                                                      reviewerService,
                                                      professorService);
    }

    @Test
    @DisplayName("Ein geladener Student wurde gefunden")
    void test_01() {
        // Arrange
        String username = "student123";
        Student student = new Student.StudentBuilder()
                .id(null)
                .name(username)
                .build();
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
    @DisplayName("Ein geladener Reviewer wurde gefunden")
    void test_03() {
        // Arrange
        String username = "reviewer123";
        Reviewer reviewer = new Reviewer.ReviewerBuilder()
                .id(null)
                .name(username)
                .build();
        when(reviewerService.getReviewerByName(username)).thenReturn(Optional.of(reviewer));

        // Act
        boolean result = userCreationService.checkReviewer(username);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Ein geladener Reviewer wurde nicht gefunden")
    void test_04() {
        // Arrange
        String username = "reviewer123";
        when(reviewerService.getReviewerByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkReviewer(username);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Ein geladener Professor wurde gefunden")
    void test_05() {
        // Arrange
        String username = "professor123";
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .name(username)
                .build();
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
        verify(reviewerService).saveReviewer(login);
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
}
