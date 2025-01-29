package exambyte.persistence.service;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.persistence.JDBC.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentEntityJDBC saveStudent(StudentEntityJDBC student) {
        return studentRepository.save(student);
    }

    public Optional<StudentEntityJDBC> findStudentById(Long id) {
        Optional<StudentEntityJDBC> student = studentRepository.findById(id);
        return student.stream().filter(b -> b.getId().equals(id)).findAny();
    }
}
