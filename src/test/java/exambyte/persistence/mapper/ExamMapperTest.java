package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ExamEntity;
import exambyte.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ExamMapperTest {

    @Test
    @DisplayName("ExamMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        ExamMapper examMapper = new ExamMapper();
        Professor professor = Professor.of(null, null, "Dr. Scalper");
        Exam exam = Exam.of(null, null, "Test 1", professor.uuid());

        // Act
        ExamEntity examEntity = examMapper.toEntity(exam);
        UUID entityFachId = examEntity.getFachId();
        String entityTitle = examEntity.getTitle();
        UUID professorFachId = examEntity.getProfessorFachId();

        // Assert
        assertThat(examEntity).isNotNull();
        assertThat(entityFachId).isEqualTo(exam.getFachId());
        assertThat(entityTitle).isEqualTo("Test 1");
        assertThat(professorFachId).isEqualTo(professor.uuid());
    }

    @Test
    @DisplayName("ExamMapper test 'toDomain")
    public void test_02() {
        // Arrange
        ExamMapper examMapper = new ExamMapper();
        ProfessorEntity professorEntity = new ProfessorEntity(null, null, "Dr. F");
        ExamEntity examEntity = new ExamEntity(
                null,
                null,
                "Test 2",
                professorEntity.getFachId());

        // Act
        Exam exam = examMapper.toDomain(examEntity);
        UUID examFachId = exam.getFachId();
        String examTitle = exam.getTitle();

        // Assert
        assertThat(exam).isNotNull();
        assertThat(examFachId).isEqualTo(examEntity.getFachId());
        assertThat(examTitle).isEqualTo("Test 2");
        assertThat(exam.getProfessorFachId()).isEqualTo(professorEntity.getFachId());
    }
}
