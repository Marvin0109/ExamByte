package exambyte.application.mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.domain.model.exam.Answer;

import java.util.List;

public interface AnswerDTOMapper {

    AnswerDTO toDTO(Answer answer);

    Answer toDomain(AnswerDTO dto);

    List<AnswerDTO> toAnswerDTOList(List<Answer> answers);
}
