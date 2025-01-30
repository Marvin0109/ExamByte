package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.service.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final SpringDataStudentRepository springDataStudentRepository;

    public StudentRepositoryImpl(SpringDataStudentRepository springDataStudentRepository) {
        this.springDataStudentRepository = springDataStudentRepository;
    }

    @Override
    public Optional<StudentEntityJDBC> findByFachId(UUID id) {
        return springDataStudentRepository.findByFachId(id);
    }

    @Override
    public void save(StudentEntityJDBC studentEntityJDBC) {
        springDataStudentRepository.save(studentEntityJDBC);
    }
}
