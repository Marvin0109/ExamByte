package exambyte.infrastructure.service;

import exambyte.application.service.user.UserCreationService;
import exambyte.application.service.user.UserCreationServiceImpl;
import exambyte.domain.model.user.Reviewer;
import exambyte.domain.model.user.Professor;
import exambyte.domain.model.user.Student;
import exambyte.domain.service.ReviewerService;
import exambyte.domain.service.ProfessorService;
import exambyte.domain.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
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
    void checkStudent_success() {
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
    void checkStudent_notFound() {
        // Arrange
        String username = "student123";
        when(studentService.getStudentByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkStudent(username);

        // Assert
        assertFalse(result);
    }

    @Test
    void checkReviewer_success() {
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
    void checkReviewer_notFound() {
        // Arrange
        String username = "reviewer123";
        when(reviewerService.getReviewerByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkReviewer(username);

        // Assert
        assertFalse(result);
    }

    @Test
    void checkProfessor_success() {
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
    void checkProfessor_notFound() {
        // Arrange
        String username = "professor123";
        when(professorService.getProfessorByName(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userCreationService.checkProfessor(username);

        // Assert
        assertFalse(result);
    }

    @Test
    void create_user_with_role_admin() {
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
    void create_user_with_role_reviewer() {
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
    void create_user_with_role_student() {
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
