package exambyte.web.service;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record PreparedQuestionData(
        QuestionDTO question,
        AnswerDTO answer,
        CorrectAnswersDTO correctAnswers) {}
