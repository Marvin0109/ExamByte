package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FrageMapperTest {

    private final FrageMapper mapper = new FrageMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        UUID examId = UUID.randomUUID();
        Frage frage = new Frage.FrageBuilder()
                .frageText("Fragetext")
                .maxPunkte(5.5)
                .type(QuestionType.FREITEXT)
                .examId(examId)
                .build();

        // Act
        FrageEntity entity = mapper.toEntity(frage);

        // Assert
        assertThat(entity.getFrageText()).isEqualTo("Fragetext");
        assertThat(entity.getMaxPunkte()).isEqualTo(11);
        assertThat(entity.getType()).isEqualTo(QuestionTypeEntity.FREITEXT);
        assertThat(entity.getExamId()).isEqualTo(examId);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID examId = UUID.randomUUID();
        FrageEntity frageEntity = new FrageEntity.FrageEntityBuilder()
                .frageText("Fragetext")
                .maxPunkte(5)
                .type(QuestionTypeEntity.FREITEXT)
                .examId(examId)
                .build();

        // Act
        Frage frage = mapper.toDomain(frageEntity);

        // Assert
        assertThat(frage.getFrageText()).isEqualTo("Fragetext");
        assertThat(frage.getType()).isEqualTo(QuestionType.FREITEXT);
        assertThat(frage.getMaxPunkte()).isCloseTo(2.5, Offset.offset(0.001));
        assertThat(frage.getExamId()).isEqualTo(examId);
    }
}
