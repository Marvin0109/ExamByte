package exambyte.domain.mapper;

import exambyte.application.dto.QuestionDTO;
import exambyte.domain.model.aggregate.exam.Question;

import java.util.List;

public interface QuestionDTOMapper {

    QuestionDTO toDTO(Question question);

    List<QuestionDTO> toQuestionDTOList(List<Question> questions);

    Question toDomain(QuestionDTO questionDTO);
}
