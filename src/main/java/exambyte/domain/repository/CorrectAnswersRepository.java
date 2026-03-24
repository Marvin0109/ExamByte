package exambyte.domain.repository;

import exambyte.domain.model.exam.CorrectAnswers;

import java.util.Optional;
import java.util.UUID;

public interface CorrectAnswersRepository {

    Optional<CorrectAnswers> findById(UUID id);

    Optional<CorrectAnswers> findByQuestionId(UUID id);

    void save(CorrectAnswers correctAnswers);

    void deleteAll();
}
