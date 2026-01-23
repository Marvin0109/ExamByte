package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FrageMapperTest {

    private final FrageMapper mapper = new FrageMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        UUID profFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        Frage frage = new Frage.FrageBuilder()
                .frageText("Fragetext")
                .maxPunkte(5)
                .type(QuestionType.FREITEXT)
                .professorUUID(profFachId)
                .examUUID(examFachId)
                .build();

        // Act
        FrageEntity entity = mapper.toEntity(frage);

        // Assert
        assertThat(entity.getFrageText()).isEqualTo("Fragetext");
        assertThat(entity.getMaxPunkte()).isEqualTo(5);
        assertThat(entity.getType()).isEqualTo(QuestionTypeEntity.FREITEXT);
        assertThat(entity.getProfessorFachId()).isEqualTo(profFachId);
        assertThat(entity.getExamFachId()).isEqualTo(examFachId);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID professorFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        FrageEntity frageEntity = new FrageEntity.FrageEntityBuilder()
                .frageText("Fragetext")
                .maxPunkte(5)
                .type(QuestionTypeEntity.FREITEXT)
                .professorFachId(professorFachId)
                .examFachId(examFachId)
                .build();

        // Act
        Frage frage = mapper.toDomain(frageEntity);

        assertThat(frage.getFrageText()).isEqualTo("Fragetext");
        assertThat(frage.getType()).isEqualTo(QuestionType.FREITEXT);
        assertThat(frage.getMaxPunkte()).isEqualTo(5);
        assertThat(frage.getProfessorUUID()).isEqualTo(professorFachId);
        assertThat(frage.getExamUUID()).isEqualTo(examFachId);
    }
}
