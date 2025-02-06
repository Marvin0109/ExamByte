package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.persistence.entities.ProfessorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProfessorMapperTest {

    @Autowired
    private ProfessorMapper professorMapper;

    @Test
    @DisplayName("ProfessorMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name("Dr. Scalper")
                .build();

        // Act
        ProfessorEntity professorEntity = professorMapper.toEntity(professor);
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
        ProfessorEntity professorEntity = new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(null)
                .name("Dr. J")
                .build();

        // Act
        Professor professor = professorMapper.toDomain(professorEntity);
        UUID professorFachId = professor.uuid();
        String professorName = professor.getName();

        // Assert
        assertThat(professor).isNotNull();
        assertThat(professorFachId).isEqualTo(professorEntity.getFachId());
        assertThat(professorName).isEqualTo("Dr. J");
    }
}
