package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FrageMapperTest {

    private final FrageMapper mapper = new FrageMapperImpl();

    @Test
    @DisplayName("FrageMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        UUID profFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(null)
                .frageText("Fragetext")
                .maxPunkte(5)
                .professorUUID(profFachId)
                .examUUID(examFachId)
                .build();

        // Act
        FrageEntity entity = mapper.toEntity(frage);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getFrageText()).isEqualTo("Fragetext");
        assertThat(entity.getMaxPunkte()).isEqualTo(5);
        assertThat(entity.getProfessorFachId()).isEqualTo(profFachId);
        assertThat(entity.getExamFachId()).isEqualTo(examFachId);
    }

    @Test
    @DisplayName("FrageMapper test 'toDomain'")
    public void test_02() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID professorFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        FrageEntity frageEntity = new FrageEntity.FrageEntityBuilder()
                .id(null)
                .fachId(fachId)
                .frageText("Fragetext")
                .maxPunkte(5)
                .professorFachId(professorFachId)
                .examFachId(examFachId)
                .build();

        // Act
        Frage frage = mapper.toDomain(frageEntity);

        assertThat(frage).isNotNull();
        assertThat(frage.getFachId()).isEqualTo(fachId);
        assertThat(frage.getFrageText()).isEqualTo("Fragetext");
        assertThat(frage.getMaxPunkte()).isEqualTo(5);
        assertThat(frage.getProfessorUUID()).isEqualTo(professorFachId);
    }
}
