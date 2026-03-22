package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.entitymapper.QuestionMapper;
import exambyte.infrastructure.persistence.entities.QuestionEntity;
import exambyte.domain.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionMapper questionMapper;
    private final QuestionDAO questionDAO;

    @Autowired
    public QuestionRepositoryImpl(QuestionDAO questionDAO, QuestionMapper questionMapper) {
        this.questionDAO = questionDAO;
        this.questionMapper = questionMapper;
    }

    @Override
    public Collection<Question> findAll() {
        return questionDAO.findAll()
                .stream()
                .map(questionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Question> findById(UUID id) {
        Optional<QuestionEntity> entity = questionDAO.findById(id);
        return entity.map(questionMapper::toDomain);
    }

    @Override
    public UUID save(Question question) {
        QuestionEntity entity = questionMapper.toEntity(question);
        questionDAO.save(entity);
        return entity.getId();
    }

    @Override
    public List<Question> findByExamId(UUID examId) {
        return questionDAO.findByExamId(examId).stream()
                .map(questionMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        questionDAO.deleteAll();
    }
}
