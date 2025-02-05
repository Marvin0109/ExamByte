package exambyte.application.service;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.aggregate.user.Student;
import exambyte.service.impl.KorrektorServiceImpl;
import exambyte.service.impl.ProfessorServiceImpl;
import exambyte.service.impl.StudentServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserCreationService {

    private final StudentServiceImpl studentServiceImpl;
    private final KorrektorServiceImpl korrektorServiceImpl;
    private final ProfessorServiceImpl professorServiceImpl;

    public UserCreationService(StudentServiceImpl studentServiceImpl,
                               KorrektorServiceImpl korrektorServiceImpl,
                               ProfessorServiceImpl professorServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
        this.korrektorServiceImpl = korrektorServiceImpl;
        this.professorServiceImpl = professorServiceImpl;
    }

    public boolean checkStudent(String username) {
        return studentServiceImpl.getStudentByName(username) != null;
    }

    public boolean checkKorrektor(String username) {
        return korrektorServiceImpl.getKorrektorByName(username) != null;
    }

    public boolean checkProfessor(String username) {
        return professorServiceImpl.getProfessorByName(username) != null;
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
        korrektorServiceImpl.saveKorrektor(korrektor);
    }

    private void createProfessor(String name) {
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        professorServiceImpl.saveProfessor(professor);
    }

    private void createStudent(String name) {
        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        studentServiceImpl.saveStudent(student);
    }
}
