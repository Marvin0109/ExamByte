package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.infrastructure.persistence.entities.AnswerEntity;

public interface AnswerMapper {

    Answer toDomain(AnswerEntity answerEntity);

    AnswerEntity toEntity(Answer answer);
}
