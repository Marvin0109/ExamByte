package exambyte.service;

import exambyte.persistence.repository.SpringDataStudentRepository;
import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataStudentRepositoryImpl implements StudentRepository {

    private final SpringDataStudentRepository springDataStudentRepository;

    public SpringDataStudentRepositoryImpl(SpringDataStudentRepository springDataStudentRepository) {
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
