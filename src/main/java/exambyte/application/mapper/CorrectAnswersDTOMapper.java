package exambyte.application.mapper;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.model.exam.CorrectAnswers;
import org.springframework.stereotype.Component;

@Component
public class CorrectAnswersDTOMapperImpl implements CorrectAnswersDTOMapper {

    @Override
    public CorrectAnswersDTO toDTO(CorrectAnswers correctAnswers) {
        return new CorrectAnswersDTO(
                correctAnswers.getId(),
                correctAnswers.getSolution(),
                correctAnswers.getChoices(),
                correctAnswers.getQuestionId());
    }

    @Override
    public CorrectAnswers toDomain(CorrectAnswersDTO dto) {
        return new CorrectAnswers.CorrectAnswersBuilder()
                .id(dto.id())
                .questionId(dto.questionId())
                .solution(dto.solution())
                .choices(dto.choices())
                .build();
    }
}
