package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.infrastructure.persistence.entities.CorrectAnswersEntity;

public interface CorrectAnswersMapper {

    CorrectAnswers toDomain(CorrectAnswersEntity entity);

    CorrectAnswersEntity toEntity(CorrectAnswers correctAnswers);
}
