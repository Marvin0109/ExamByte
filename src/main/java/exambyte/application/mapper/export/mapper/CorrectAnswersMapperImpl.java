package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.exam.CorrectAnswers;
import exambyte.infrastructure.entity.CorrectAnswersEntity;
import org.springframework.stereotype.Component;

@Component
public class CorrectAnswersMapperImpl implements CorrectAnswersMapper {

    @Override
    public CorrectAnswers toDomain(CorrectAnswersEntity entity) {
        return new CorrectAnswers.CorrectAnswersBuilder()
                .id(entity.getId())
                .questionId(entity.getQuestionId())
                .solution(entity.getSolution())
                .choices(entity.getChoices())
                .build();
    }

    @Override
    public CorrectAnswersEntity toEntity(CorrectAnswers correctAnswers) {
    return new CorrectAnswersEntity.CorrectAnswersEntityBuilder()
            .questionId(correctAnswers.getQuestionId())
            .choices(correctAnswers.getChoices())
            .solution(correctAnswers.getSolution())
            .build();
    }
}
