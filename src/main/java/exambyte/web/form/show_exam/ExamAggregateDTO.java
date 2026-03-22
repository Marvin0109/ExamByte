package exambyte.web.form.show_exam;

import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record ExamAggregateDTO(
        QuestionDTO question,
        CorrectAnswersDTO correctAnswers) {}
