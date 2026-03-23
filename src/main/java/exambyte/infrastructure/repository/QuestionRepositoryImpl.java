package exambyte.infrastructure.repository;

import exambyte.domain.model.exam.Question;
import exambyte.infrastructure.mapper.QuestionMapper;
import exambyte.infrastructure.entity.QuestionEntity;
import exambyte.domain.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionMapper mapper;
    private final QuestionDAO dao;

    @Autowired
    public QuestionRepositoryImpl(QuestionDAO dao, QuestionMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public Collection<Question> findAll() {
        return dao.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Question> findById(UUID id) {
        Optional<QuestionEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public UUID save(Question question) {
        QuestionEntity entity = mapper.toEntity(question);
        dao.save(entity);
        return entity.getId();
    }

    @Override
    public List<Question> findByExamId(UUID examId) {
        return dao.findByExamId(examId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
