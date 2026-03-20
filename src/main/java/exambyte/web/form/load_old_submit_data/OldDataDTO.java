package exambyte.web.form.load_old_submit_data;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record OldDataDTO(
        FrageDTO fragen,
        CorrectAnswersDTO correctAnswers,
        AnswerDTO answer) {}
