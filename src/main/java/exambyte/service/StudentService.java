package exambyte.service;

import exambyte.domain.aggregate.user.Student;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudent(UUID fachId) {
        return studentRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    public UUID saveStudent(Student student) {
        studentRepository.save(student);
        return student.uuid();
    }
}
