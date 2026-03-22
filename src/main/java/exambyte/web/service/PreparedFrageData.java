package exambyte.web.service;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record PreparedFrageData(
        QuestionDTO frage,
        AnswerDTO answer,
        CorrectAnswersDTO correctAnswers) {}
