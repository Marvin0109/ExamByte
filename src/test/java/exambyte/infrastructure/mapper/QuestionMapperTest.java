package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Question;
import exambyte.domain.model.enums.QuestionType;
import exambyte.infrastructure.common.QuestionTypeEntity;
import exambyte.infrastructure.entity.QuestionEntity;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionMapperTest {

    private final QuestionMapper mapper = new QuestionMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        UUID examId = UUID.randomUUID();
        Question question = new Question.FrageBuilder()
                .text("Question")
                .points(5.5)
                .type(QuestionType.FREE_RESPONSE)
                .examId(examId)
                .build();

        // Act
        QuestionEntity entity = mapper.toEntity(question);

        // Assert
        assertThat(entity.getText()).isEqualTo("Question");
        assertThat(entity.getPoints()).isEqualTo(11);
        assertThat(entity.getType()).isEqualTo(QuestionTypeEntity.FREE_RESPONSE);
        assertThat(entity.getExamId()).isEqualTo(examId);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID examId = UUID.randomUUID();
        QuestionEntity entity = new QuestionEntity.QuestionEntityBuilder()
                .text("Question")
                .points(5)
                .type(QuestionTypeEntity.FREE_RESPONSE)
                .examId(examId)
                .build();

        // Act
        Question question = mapper.toDomain(entity);

        // Assert
        assertThat(question.getText()).isEqualTo("Question");
        assertThat(question.getType()).isEqualTo(QuestionType.FREE_RESPONSE);
        assertThat(question.getPoints()).isCloseTo(2.5, Offset.offset(0.001));
        assertThat(question.getExamId()).isEqualTo(examId);
    }
}
