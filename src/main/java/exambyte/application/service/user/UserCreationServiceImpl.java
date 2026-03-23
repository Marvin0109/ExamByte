package exambyte.application.service.user;

import exambyte.application.service.query.ProfessorService;
import exambyte.domain.service.ReviewerService;
import exambyte.domain.service.StudentService;
import exambyte.application.common.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserCreationServiceImpl implements UserCreationService {

    private final StudentService studentService;
    private final ReviewerService reviewerService;
    private final ProfessorService professorService;

    public UserCreationServiceImpl(StudentService studentService,
                                   ReviewerService reviewerService,
                                   ProfessorService professorService) {
        this.studentService = studentService;
        this.reviewerService = reviewerService;
        this.professorService = professorService;
    }

    public boolean checkStudent(String username) {
        return studentService.getStudentByName(username).isPresent();
    }

    public boolean checkReviewer(String username) {
        return reviewerService.getReviewerByName(username).isPresent();
    }

    public boolean checkProfessor(String username) {
        return professorService.getProfessorByName(username).isPresent();
    }

    public void createUser(OAuth2User user, Set<GrantedAuthority> authorities) {
        String name = user.getAttribute("login");
        Set<Role> roles = extractRoles(authorities);

        for (Role role : roles) {
            switch (role) {
                case Role.ADMIN:
                    if (!checkProfessor(name)) {
                        createProfessor(name);
                    }
                    break;
                case Role.REVIEWER:
                    if (!checkReviewer(name)) {
                        createReviewer(name);
                    }
                    break;
                case Role.STUDENT:
                    if (!checkStudent(name)) {
                        createStudent(name);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Set<Role> extractRoles(Set<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> Role.valueOf(a.substring(5)))
                .collect(Collectors.toSet());
    }

    private void createReviewer(String name) {
        reviewerService.saveReviewer(name);
    }

    private void createProfessor(String name) {
        professorService.saveProfessor(name);
    }

    private void createStudent(String name) {
        studentService.saveStudent(name);
    }
}
