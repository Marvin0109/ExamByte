package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.repository.StudentRepository;
import exambyte.infrastructure.exceptions.NotFoundException;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student getStudent(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
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
    public UUID getStudentId(String name) {
        Optional<UUID> loadedID = repository.findIdByName(name);
        return loadedID.orElse(null);
    }
}
