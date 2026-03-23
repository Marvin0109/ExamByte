package exambyte.infrastructure.repository;

import exambyte.domain.model.user.Professor;
import exambyte.infrastructure.mapper.ProfessorMapper;
import exambyte.infrastructure.entity.ProfessorEntity;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final ProfessorMapper mapper;
    private final ProfessorDAO dao;

    public ProfessorRepositoryImpl(ProfessorDAO dao,
                                   ProfessorMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public Optional<Professor> findById(UUID id) {
        Optional<ProfessorEntity> entity = dao.findById(id);
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

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
