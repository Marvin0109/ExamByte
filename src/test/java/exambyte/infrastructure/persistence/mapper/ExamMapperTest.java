package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.entitymapper.ExamMapper;
import exambyte.infrastructure.persistence.entities.ExamEntity;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExamMapperTest {

    private final ExamMapper mapper = new ExamMapperImpl();

    @Test
    @DisplayName("ExamMapper test 'toEntity'")
    void test_01() {
        // Arrange
        Professor professor = new Professor.ProfessorBuilder()
            .id(null)
            .fachId(null)
            .name("Dr. Scalper")
            .build();

        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 2, 14, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 7, 9, 14, 0);

        Exam exam = new Exam.ExamBuilder()
                .id(null)
                .fachId(null)
                .title("Test 1")
                .professorFachId(professor.uuid())
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        // Act
        ExamEntity examEntity = mapper.toEntity(exam);
        UUID entityFachId = examEntity.getFachId();
        String entityTitle = examEntity.getTitle();
        UUID professorFachId = examEntity.getProfessorFachId();

        // Assert
        assertThat(examEntity).isNotNull();
        assertThat(entityFachId).isEqualTo(exam.getFachId());
        assertThat(entityTitle).isEqualTo("Test 1");
        assertThat(professorFachId).isEqualTo(professor.uuid());
        assertThat(examEntity.getStartZeitpunkt()).isEqualTo(startTime);
        assertThat(examEntity.getEndZeitpunkt()).isEqualTo(endTime);
        assertThat(examEntity.getResultZeitpunkt()).isEqualTo(resultTime);
    }

    @Test
    @DisplayName("ExamMapper test 'toDomain")
    void test_02() {
        // Arrange
        ProfessorEntity professorEntity = new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(null)
                .name("Dr. F")
                .build();

        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 2, 14, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 7, 9, 14, 0);
        ExamEntity examEntity = new ExamEntity.ExamEntityBuilder()
                .id(null)
                .fachId(null)
                .title("Test 2")
                .professorFachId(professorEntity.getFachId())
                .startZeitpunkt(startTime)
                .endZeitpunkt(endTime)
                .resultZeitpunkt(resultTime)
                .build();

        // Act
        Exam exam = mapper.toDomain(examEntity);
        UUID examFachId = exam.getFachId();
        String examTitle = exam.getTitle();

        // Assert
        assertThat(exam).isNotNull();
        assertThat(examFachId).isEqualTo(examEntity.getFachId());
        assertThat(examTitle).isEqualTo("Test 2");
        assertThat(exam.getProfessorFachId()).isEqualTo(professorEntity.getFachId());
        assertThat(examEntity.getStartZeitpunkt()).isEqualTo(startTime);
        assertThat(examEntity.getEndZeitpunkt()).isEqualTo(endTime);
        assertThat(examEntity.getResultZeitpunkt()).isEqualTo(resultTime);
    }
}
