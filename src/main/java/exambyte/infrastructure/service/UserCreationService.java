package exambyte.infrastructure.service;

import exambyte.domain.service.KorrektorService;
import exambyte.domain.service.ProfessorService;
import exambyte.domain.service.StudentService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserCreationService {

    private final StudentService studentService;
    private final KorrektorService korrektorService;
    private final ProfessorService professorService;

    public UserCreationService(StudentService studentService,
                               KorrektorService korrektorService,
                               ProfessorService professorService) {
        this.studentService = studentService;
        this.korrektorService = korrektorService;
        this.professorService = professorService;
    }

    public boolean checkStudent(String username) {
        return studentService.getStudentByName(username).isPresent();
    }

    public boolean checkKorrektor(String username) {
        return korrektorService.getKorrektorByName(username).isPresent();
    }

    public boolean checkProfessor(String username) {
        return professorService.getProfessorByName(username).isPresent();
    }

    @Transactional
    public void createUser(OAuth2User user, Set<GrantedAuthority> authorities) {
        String name = user.getAttribute("login");

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            createProfessor(name);
        }
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_REVIEWER"))) {
            createKorrektor(name);
        }
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            createStudent(name);
        }
    }

    private void createKorrektor(String name) {
        korrektorService.saveKorrektor(name);
    }

    private void createProfessor(String name) {
        professorService.saveProfessor(name);
    }

    private void createStudent(String name) {
        studentService.saveStudent(name);
    }
}
