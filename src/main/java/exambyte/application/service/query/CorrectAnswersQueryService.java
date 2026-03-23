package exambyte.application.service.query;

import exambyte.application.dto.CorrectAnswersDTO;

import java.util.UUID;

public interface CorrectAnswersQueryService {

    CorrectAnswersDTO getCorrectAnswerForQuestion(UUID questionId);

    void deleteAll();

    void addCorrectAnswers(CorrectAnswersDTO correctAnswers);
}
