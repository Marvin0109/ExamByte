package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.service.ProfessorRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final SpringDataProfessorRepository springDataProfessorRepository;

    public ProfessorRepositoryImpl(@Lazy SpringDataProfessorRepository springDataProfessorRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
    }

    @Override
    public Optional<ProfessorEntityJDBC> findByFachId(UUID id) {
        return springDataProfessorRepository.findByFachId(id);
    }

    @Override
    public void save(ProfessorEntityJDBC professor) {
        springDataProfessorRepository.save(professor);
    }
}
