package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.service.ProfessorRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final SpringDataProfessorRepository springDataProfessorRepository;

    public ProfessorRepositoryImpl(@Lazy SpringDataProfessorRepository springDataProfessorRepository) {
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
