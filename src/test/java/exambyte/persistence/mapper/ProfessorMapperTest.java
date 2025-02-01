package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfessorMapperTest {

    @Test
    @DisplayName("ProfessorMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        ProfessorMapper profMapper = new ProfessorMapper();
        Professor professor = Professor.of(null, null, "Dr. Scalper");

        // Act
        ProfessorEntity professorEntity = profMapper.toEntity(professor);
        UUID entityFachId = professorEntity.getFachId();
        String entityName = professorEntity.getName();

        // Assert
        assertThat(professorEntity).isNotNull();
        assertThat(entityFachId).isEqualTo(professor.uuid());
        assertThat(entityName).isEqualTo("Dr. Scalper");
    }

    @Test
    @DisplayName("ProfessorMapper test 'toDomain'")
    public void test_02() {
        // Arrange
        ProfessorMapper profMapper = new ProfessorMapper();
        ProfessorEntity professorEntity = new ProfessorEntity(null, null, "Dr. J");

        // Act
        Professor professor = profMapper.toDomain(professorEntity);
        UUID professorFachId = professor.uuid();
        String professorName = professor.getName();

        // Assert
        assertThat(professor).isNotNull();
        assertThat(professorFachId).isEqualTo(professorEntity.getFachId());
        assertThat(professorName).isEqualTo("Dr. J");
    }
}
