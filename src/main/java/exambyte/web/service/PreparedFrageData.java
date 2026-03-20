package exambyte.web.service;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;

public record PreparedFrageData(
        FrageDTO frage,
        AnswerDTO answer,
        CorrectAnswersDTO correctAnswers) {}
