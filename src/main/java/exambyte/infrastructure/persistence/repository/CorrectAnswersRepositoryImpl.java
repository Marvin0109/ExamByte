package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.CorrectAnswersMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.domain.repository.CorrectAnswersRepository;
import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CorrectAnswersRepositoryImpl implements CorrectAnswersRepository {

    private final CorrectAnswersDAO dao;
    private final CorrectAnswersMapper mapper;

    public CorrectAnswersRepositoryImpl(CorrectAnswersDAO dao, CorrectAnswersMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public Optional<CorrectAnswers> findByFrageId(UUID frageId) {
        Optional<CorrectAnswersEntity> entity = dao.findByFrageId(frageId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(CorrectAnswers answer) {
        CorrectAnswersEntity entity = mapper.toEntity(answer);
        dao.save(entity);
    }

    @Override
    public Optional<CorrectAnswers> findById(UUID id) {
        Optional<CorrectAnswersEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
