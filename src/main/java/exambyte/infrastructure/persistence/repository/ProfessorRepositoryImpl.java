package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final ProfessorMapper professorMapper;
    private final ProfessorDAO professorDAO;

    public ProfessorRepositoryImpl(ProfessorDAO professorDAO,
                                   ProfessorMapper professorMapper) {
        this.professorDAO = professorDAO;
        this.professorMapper = professorMapper;
    }

    @Override
    public Optional<Professor> findByFachId(UUID fachId) {
        Optional<ProfessorEntity> entity = professorDAO.findByFachId(fachId);
        return entity.map(professorMapper::toDomain);
    }

    @Override
    public void save(Professor professor) {
        ProfessorEntity entity = professorMapper.toEntity(professor);
        professorDAO.save(entity);
    }

    @Override
    public Optional<Professor> findByName(String name) {
        Optional<ProfessorEntity> entity = professorDAO.findByName(name);
        return entity.map(professorMapper::toDomain);
    }
}
