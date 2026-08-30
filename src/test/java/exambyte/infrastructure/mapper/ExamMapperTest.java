package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Exam;
import exambyte.infrastructure.entity.ExamEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExamMapperTest {

    private final ExamMapper mapper = new ExamMapper();

    @Test
    void toEntity() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = startTime.plusHours(1);
        LocalDateTime resultTime = endTime.plusHours(1);

        Exam exam = new Exam.ExamBuilder()
                .title("Test 1")
                .professorId(UUID.randomUUID())
                .start(startTime)
                .end(endTime)
                .result(resultTime)
                .build();

        // Act
        ExamEntity examEntity = mapper.toEntity(exam);

        // Assert
        assertThat(examEntity.getTitle()).isEqualTo("Test 1");
        assertThat(examEntity.getProfessorId()).isNotNull();
        assertThat(examEntity.getStart()).isEqualTo(startTime);
        assertThat(examEntity.getEnd()).isEqualTo(endTime);
        assertThat(examEntity.getResult()).isEqualTo(resultTime);
    }

    @Test
    void toDomain() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 2, 14, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 7, 9, 14, 0);
        ExamEntity examEntity = new ExamEntity.ExamEntityBuilder()
                .title("Test 2")
                .professorId(UUID.randomUUID())
                .start(startTime)
                .end(endTime)
                .result(resultTime)
                .build();

        // Act
        Exam exam = mapper.toDomain(examEntity);

        // Assert
        assertThat(exam.getTitle()).isEqualTo("Test 2");
        assertThat(exam.getProfessorId()).isNotNull();
        assertThat(examEntity.getStart()).isEqualTo(startTime);
        assertThat(examEntity.getEnd()).isEqualTo(endTime);
        assertThat(examEntity.getResult()).isEqualTo(resultTime);
    }
}
