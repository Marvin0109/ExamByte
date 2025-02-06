package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.persistence.entities.FrageEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FrageMapperTest {

    @Autowired
    private FrageMapper frageMapper;

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
        FrageEntity entity = frageMapper.toEntity(frage);

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
        Frage frage = frageMapper.toDomain(frageEntity);

        assertThat(frage).isNotNull();
        assertThat(frage.getFachId()).isEqualTo(fachId);
        assertThat(frage.getFrageText()).isEqualTo("Fragetext");
        assertThat(frage.getMaxPunkte()).isEqualTo(5);
        assertThat(frage.getProfessorUUID()).isEqualTo(professorFachId);
    }
}
