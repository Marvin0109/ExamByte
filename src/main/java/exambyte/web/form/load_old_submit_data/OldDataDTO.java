package exambyte.web.form.load_old_submit_data;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record OldDataDTO(
        QuestionDTO question,
        CorrectAnswersDTO correctAnswers,
        AnswerDTO answer) {}
