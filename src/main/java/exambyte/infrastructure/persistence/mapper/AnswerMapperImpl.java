package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.entitymapper.AnswerMapper;
import exambyte.infrastructure.persistence.entities.AnswerEntity;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public Answer toDomain(AnswerEntity entity) {

        return new Answer.AnswerBuilder()
                .id(entity.getId())
                .answer(entity.getAnswer())
                .questionId(entity.getQuestionId())
                .studentId(entity.getStudentId())
                .submitTime(entity.getSubmitTime())
                .build();
    }

    @Override
    public AnswerEntity toEntity(Answer answer) {

        return new AnswerEntity.AnswerEntityBuilder()
                .id(answer.getId())
                .answer(answer.getAnswer())
                .questionId(answer.getQuestionId())
                .studentId(answer.getStudentUUID())
                .submitTime(answer.getSubmitTime())
                .build();
    }
}
