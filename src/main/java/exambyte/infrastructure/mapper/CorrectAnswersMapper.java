package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.CorrectAnswers;
import exambyte.infrastructure.entity.CorrectAnswersEntity;

public interface CorrectAnswersMapper {

    CorrectAnswers toDomain(CorrectAnswersEntity entity);

    CorrectAnswersEntity toEntity(CorrectAnswers correctAnswers);
}
