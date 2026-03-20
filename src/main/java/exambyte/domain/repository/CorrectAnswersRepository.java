package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.CorrectAnswers;

import java.util.Optional;
import java.util.UUID;

public interface CorrectAnswersRepository {

    Optional<CorrectAnswers> findById(UUID id);

    Optional<CorrectAnswers> findByFrageId(UUID frageID);

    void save(CorrectAnswers correctAnswers);

    void deleteAll();
}
