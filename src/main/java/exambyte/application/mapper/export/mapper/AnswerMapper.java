package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.exam.Answer;
import exambyte.infrastructure.entity.AnswerEntity;

public interface AnswerMapper {

    Answer toDomain(AnswerEntity entity);

    AnswerEntity toEntity(Answer answer);
}
