package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.entitymapper.AnswerMapper;
import exambyte.infrastructure.persistence.entities.AnswerEntity;
import exambyte.domain.repository.AnswerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AnswerRepositoryImpl implements AnswerRepository {

    private final AnswerMapper mapper;
    private final AnswerDAO dao;

    public AnswerRepositoryImpl(AnswerDAO dao, AnswerMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public Optional<Answer> findById(UUID id) {
        Optional<AnswerEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public Optional<Answer> findByStudentIdAndFrageId(UUID studentId, UUID id) {
        Optional<AnswerEntity> entity = dao
                .findByStudentIdAndFrageId(studentId, id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Answer answer) {
        AnswerEntity answerEntity = mapper.toEntity(answer);
        dao.upsertAnswer(answerEntity.getStudentId(),
                answerEntity.getFrageId(),
                answerEntity.getAnswer());
    }

    @Override
    public Answer findByFrageId(UUID id) {
        Optional<AnswerEntity> entity = dao.findByFrageId(id);
        return entity.map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        dao.deleteById(id);
    }
}
