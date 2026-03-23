package exambyte.application.mapper;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.model.exam.CorrectAnswers;

public interface CorrectAnswersDTOMapper {

    CorrectAnswersDTO toDTO(CorrectAnswers correctAnswers);
    CorrectAnswers toDomain(CorrectAnswersDTO dto);
}
