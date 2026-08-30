package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Question;
import exambyte.domain.model.enums.QuestionType;
import exambyte.infrastructure.enums.QuestionTypeEntity;
import exambyte.infrastructure.entity.QuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public Question toDomain(QuestionEntity entity) {

        return new Question.FrageBuilder()
                .id(entity.getId())
                .text(entity.getText())
                .points(entity.getPoints() / 2.0)
                .type(QuestionType.valueOf(entity.getType().name()))
                .examId(entity.getExamId())
                .build();
    }

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
