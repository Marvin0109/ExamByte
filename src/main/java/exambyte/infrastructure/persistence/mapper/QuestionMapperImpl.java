package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.entitymapper.QuestionMapper;
import exambyte.domain.model.common.QuestionType;
import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import exambyte.infrastructure.persistence.entities.QuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public Question toDomain(QuestionEntity entity) {

        return new Question.FrageBuilder()
                .id(entity.getId())
                .text(entity.getText())
                .points(entity.getPoints() / 2.0)
                .type(QuestionType.valueOf(entity.getType().name()))
                .examId(entity.getExamId())
                .build();
    }

    @Override
    public QuestionEntity toEntity(Question question) {
        int pointsForDb = (int) Math.round(question.getPoints() * 2.0);

        return new QuestionEntity.QuestionEntityBuilder()
                .id(question.getId())
                .text(question.getText())
                .points(pointsForDb)
                .type(QuestionTypeEntity.valueOf(question.getType().name()))
                .examId(question.getExamId())
                .build();
    }
}
