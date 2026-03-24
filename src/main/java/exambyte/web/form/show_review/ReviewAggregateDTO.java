package exambyte.web.form.show_review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;

public record ReviewAggregateDTO(
        QuestionDTO question,
        AnswerDTO answer,
        ReviewDTO review,
        CorrectAnswersDTO correctAnswers) {}
