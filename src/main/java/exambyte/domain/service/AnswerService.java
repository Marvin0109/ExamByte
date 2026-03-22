package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Answer;

import java.util.UUID;

public interface AnswerService {

    Answer findByQuestionId(UUID id);

    void addAnswer(Answer answer);

    Answer findByStudentIdAndQuestionId(UUID studentId, UUID questionId);

    void deleteAll();

    void deleteAnswer(UUID id);
}
