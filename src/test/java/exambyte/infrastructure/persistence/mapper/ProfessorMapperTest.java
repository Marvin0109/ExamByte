package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessorMapperTest {

    private final ProfessorMapper professorMapper = new ProfessorMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        Professor professor = new Professor.ProfessorBuilder()
                .name("Dr. Scalper")
                .build();

        // Act
        ProfessorEntity professorEntity = professorMapper.toEntity(professor);

        // Assert
        assertThat(professorEntity.getName()).isEqualTo("Dr. Scalper");
    }

    @Test
    @DisplayName("ProfessorMapper test 'toDomain'")
    void toDomain() {
        // Arrange
        ProfessorEntity professorEntity = new ProfessorEntity.ProfessorEntityBuilder()
                .name("Dr. J")
                .build();

        // Act
        Professor professor = professorMapper.toDomain(professorEntity);

        // Assert
        assertThat(professor.getName()).isEqualTo("Dr. J");
    }
}
