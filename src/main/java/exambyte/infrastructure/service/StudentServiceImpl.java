package exambyte.infrastructure.service;

import exambyte.domain.aggregate.user.Student;
import exambyte.domain.repository.StudentRepository;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudent(UUID fachId) {
        return studentRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveStudent(String name) {
        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();
        studentRepository.save(student);
    }

    @Override
    public Optional<Student> getStudentByName(String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public UUID getStudentFachId(String name) {
        return studentRepository.findFachIdByName(name);
    }
}
