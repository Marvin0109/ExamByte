package exambyte.domain.mapper;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;

public interface CorrectAnswersDTOMapper {

    CorrectAnswersDTO toDTO(CorrectAnswers correctAnswers);
    CorrectAnswers toDomain(CorrectAnswersDTO dto);
}
