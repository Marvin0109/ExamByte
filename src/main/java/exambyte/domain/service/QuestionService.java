package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionService {

    List<Question> getQuestionsForExam(UUID examId);

    UUID addQuestion(Question question);

    Optional<Question> getQuestion(UUID questionId);

    void deleteAll();
}
