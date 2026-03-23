package exambyte.infrastructure.mapper;

import exambyte.domain.model.user.Professor;
import exambyte.application.mapper.export.mapper.ProfessorMapper;
import exambyte.application.mapper.export.mapper.ProfessorMapperImpl;
import exambyte.infrastructure.entity.ProfessorEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessorMapperTest {

    private final ProfessorMapper mapper = new ProfessorMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        Professor professor = new Professor.ProfessorBuilder()
                .name("Dr. Scalper")
                .build();

        // Act
        ProfessorEntity professorEntity = mapper.toEntity(professor);

        // Assert
        assertThat(professorEntity.getName()).isEqualTo("Dr. Scalper");
    }

    @Test
    void toDomain() {
        // Arrange
        ProfessorEntity professorEntity = new ProfessorEntity.ProfessorEntityBuilder()
                .name("Dr. J")
                .build();

        // Act
        Professor professor = mapper.toDomain(professorEntity);

        // Assert
        assertThat(professor.getName()).isEqualTo("Dr. J");
    }
}
