package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.entitymapper.AnswerMapper;
import exambyte.infrastructure.persistence.entities.AnswerEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerMapperTest {

    private final AnswerMapper mapper = new AnswerMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        UUID questionId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2025, 1, 1, 12, 0);

        Answer answer = new Answer.AnswerBuilder()
            .answer("Answer")
            .questionId(questionId)
            .studentId(studentId)
            .submitTime(submitTime)
            .build();

        // Act
        AnswerEntity answerEntity = mapper.toEntity(answer);

        // Assert
        assertThat(answerEntity.getAnswer()).isEqualTo("Answer");
        assertThat(answerEntity.getQuestionId()).isEqualTo(questionId);
        assertThat(answerEntity.getStudentId()).isEqualTo(studentId);
        assertThat(answerEntity.getSubmitTime()).isEqualTo(submitTime);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID questionId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime submitTime = LocalDateTime.of(2025, 1, 1, 12, 0);

        AnswerEntity answerEntity = new AnswerEntity.AnswerEntityBuilder()
            .answer("Answer")
            .questionId(questionId)
            .studentId(studentId)
            .submitTime(submitTime)
            .build();

        // Act
        Answer answer = mapper.toDomain(answerEntity);

        // Assert
        assertThat(answer.getAnswer()).isEqualTo("Answer");
        assertThat(answer.getQuestionId()).isEqualTo(questionId);
        assertThat(answer.getStudentUUID()).isEqualTo(studentId);
        assertThat(answer.getSubmitTime()).isEqualTo(submitTime);
    }
}
