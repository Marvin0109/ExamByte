package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.repository.StudentRepository;
import exambyte.infrastructure.exceptions.NichtVorhandenException;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    @Override
    public Student getStudent(UUID fachId) {
        return repository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveStudent(String name) {
        Student student = new Student.StudentBuilder()
                .name(name)
                .build();
        repository.save(student);
    }

    @Override
    public Optional<Student> getStudentByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public UUID getStudentFachId(String name) {
        Optional<UUID> loadedFachID = repository.findFachIdByName(name);
        return loadedFachID.orElse(null);
    }
}
