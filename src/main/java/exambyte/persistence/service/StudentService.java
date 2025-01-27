package exambyte.persistence.service;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.persistence.JDBC.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentEntityJDBC saveStudent(StudentEntityJDBC student) {
        return studentRepository.save(student);
    }

    public Optional<StudentEntityJDBC> findStudentById(Long id) {
        return studentRepository.findById(id);
    }
}
