package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.service.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final SpringDataStudentRepository springDataStudentRepository;

    public StudentRepositoryImpl(SpringDataStudentRepository springDataStudentRepository) {
        this.springDataStudentRepository = springDataStudentRepository;
    }

    @Override
    public Optional<StudentEntityJDBC> findById(Long id) {
        return springDataStudentRepository.findById(id);
    }

    @Override
    public void save(StudentEntityJDBC studentEntityJDBC) {
        springDataStudentRepository.save(studentEntityJDBC);
    }
}
