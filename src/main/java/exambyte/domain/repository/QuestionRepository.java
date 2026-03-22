package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Question;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository {

    Collection<Question> findAll();

    Optional<Question> findById(UUID id);

    List<Question> findByExamId(UUID examId);

    UUID save(Question question);

    void deleteAll();
}
