package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.CorrectAnswers;

import java.util.UUID;

public interface CorrectAnswersService {
    void addCorrectAnswer(CorrectAnswers correctAnswers);
    CorrectAnswers findSolution(UUID frageId);
    void deleteAll();
}
