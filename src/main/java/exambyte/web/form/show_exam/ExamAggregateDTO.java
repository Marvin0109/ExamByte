package exambyte.web.form.show_exam;

import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record ExamAggregateDTO(
        FrageDTO frage,
        CorrectAnswersDTO correctAnswers) {}
