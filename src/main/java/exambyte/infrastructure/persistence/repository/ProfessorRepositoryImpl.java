package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final ProfessorMapper mapper;
    private final ProfessorDAO dao;

    public ProfessorRepositoryImpl(ProfessorDAO professorDAO,
                                   ProfessorMapper professorMapper) {
        this.dao = professorDAO;
        this.mapper = professorMapper;
    }

    @Override
    public Optional<Professor> findByFachId(UUID fachId) {
        Optional<ProfessorEntity> entity = dao.findByFachId(fachId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Professor professor) {
        ProfessorEntity entity = mapper.toEntity(professor);
        dao.save(entity);
    }

    @Override
    public Optional<Professor> findByName(String name) {
        Optional<ProfessorEntity> entity = dao.findByName(name);
        return entity.map(mapper::toDomain);
    }
}
