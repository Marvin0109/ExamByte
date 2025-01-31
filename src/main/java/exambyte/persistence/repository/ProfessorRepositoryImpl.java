package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.service.ProfessorRepository;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final SpringDataProfessorRepository springDataProfessorRepository;

    public ProfessorRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
    }

    @Override
    public Optional<ProfessorEntityJDBC> findByFachId(UUID fachId) {
        return springDataProfessorRepository.findByFachId(fachId);
    }

    @Override
    public void save(ProfessorEntityJDBC professorEntityJDBC) {
        springDataProfessorRepository.save(professorEntityJDBC);
    }

    public ProfessorEntityJDBC findByProfFachId(UUID fachId) {
        Optional<ProfessorEntityJDBC> existingProfessor = findByFachId(fachId);

        if (existingProfessor.isPresent()) {
            return existingProfessor.get();
        }

        ProfessorEntityJDBC newProfessor = new ProfessorEntityJDBC(null, fachId, "N/A");
        save(newProfessor);
        return newProfessor;
    }
}
