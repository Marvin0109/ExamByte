package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Question;
import exambyte.infrastructure.entity.QuestionEntity;

public interface QuestionMapper {

    Question toDomain(QuestionEntity entity);

    QuestionEntity toEntity(Question question);
}
