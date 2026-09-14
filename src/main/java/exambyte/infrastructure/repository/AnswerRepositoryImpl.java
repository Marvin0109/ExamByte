package exambyte.infrastructure.repository;

import exambyte.domain.model.exam.Answer;
import exambyte.infrastructure.mapper.AnswerMapper;
import exambyte.infrastructure.entity.AnswerEntity;
import exambyte.domain.repository.AnswerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public Optional<Answer> findByStudentIdAndQuestionId(UUID studentId, UUID examId) {
        Optional<AnswerEntity> entity = dao
                .findByStudentIdAndQuestionId(studentId, examId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Answer answer) {
        AnswerEntity entity = mapper.toEntity(answer);
        dao.upsertAnswer(entity.getStudentId(),
                entity.getQuestionId(),
                entity.getAnswer());
    }

    @Override
    public List<Answer> findByQuestionId(UUID id) {
        List<AnswerEntity> list = dao.findByQuestionId(id);
        return list.stream()
                .map(mapper::toDomain)
                .toList();
    }
}
