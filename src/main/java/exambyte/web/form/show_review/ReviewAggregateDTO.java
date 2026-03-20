package exambyte.web.form.show_review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;

public record ReviewAggregateDTO(
        FrageDTO frage,
        AnswerDTO answer,
        ReviewDTO review,
        CorrectAnswersDTO correctAnswers) {}
