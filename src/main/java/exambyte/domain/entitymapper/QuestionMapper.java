package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.Question;
import exambyte.infrastructure.persistence.entities.QuestionEntity;

public interface QuestionMapper {

    Question toDomain(QuestionEntity entity);

    QuestionEntity toEntity(Question question);
}
