package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Answer;

import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository {

    Answer findByFrageId(UUID id);

    Optional<Answer> findById(UUID id);

    Optional<Answer> findByStudentIdAndFrageId(UUID studentFachId, UUID examFachId);

    void save(Answer answer);

    void deleteAll();

    void deleteAnswer(UUID fachId);
}
