package exambyte.application.service;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.aggregate.user.Student;
import exambyte.service.KorrektorService;
import exambyte.service.ProfessorService;
import exambyte.service.StudentService;
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
        return studentService.getStudentByName(username) != null;
    }

    public boolean checkKorrektor(String username) {
        return korrektorService.getKorrektorByName(username) != null;
    }

    public boolean checkProfessor(String username) {
        return professorService.getProfessorByName(username) != null;
    }

    @Transactional
    public void createUser(OAuth2User user, Set<GrantedAuthority> authorities) {
        String name = user.getAttribute("login");

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            createProfessor(name);
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_REVIEWER"))) {
            createKorrektor(name);
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            createStudent(name);
        }
    }

    private void createKorrektor(String name) {
        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        korrektorService.saveKorrektor(korrektor);
    }

    private void createProfessor(String name) {
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        professorService.saveProfessor(professor);
    }

    private void createStudent(String name) {
        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        studentService.saveStudent(student);
    }
}
