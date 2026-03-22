package exambyte.domain.repository;

import exambyte.domain.model.aggregate.exam.Answer;

import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository {

    Answer findByQuestionId(UUID id);

    Optional<Answer> findById(UUID id);

    Optional<Answer> findByStudentIdAndQuestionId(UUID studentId, UUID examId);

    void save(Answer answer);

    void deleteAll();

    void deleteAnswer(UUID id);
}
