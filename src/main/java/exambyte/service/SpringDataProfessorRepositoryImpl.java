package exambyte.service;

import exambyte.persistence.repository.SpringDataProfessorRepository;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataProfessorRepositoryImpl implements ProfessorRepository {

    private final SpringDataProfessorRepository springDataProfessorRepository;

    public SpringDataProfessorRepositoryImpl(@Lazy SpringDataProfessorRepository springDataProfessorRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
    }

    @Override
    public Optional<ProfessorEntityJDBC> findById(Long id) {
        return springDataProfessorRepository.findById(id);
    }

    @Override
    public void save(ProfessorEntityJDBC professor) {
        springDataProfessorRepository.save(professor);
    }
}
