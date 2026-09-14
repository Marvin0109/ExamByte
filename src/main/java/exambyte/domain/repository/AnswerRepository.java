package exambyte.domain.repository;

import exambyte.domain.model.exam.Answer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository {

    List<Answer> findByQuestionId(UUID id);

    Optional<Answer> findById(UUID id);

    Optional<Answer> findByStudentIdAndQuestionId(UUID studentId, UUID examId);

    void save(Answer answer);
}
