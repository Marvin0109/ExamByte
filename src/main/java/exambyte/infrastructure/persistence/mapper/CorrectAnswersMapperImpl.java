package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.CorrectAnswersMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;
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
