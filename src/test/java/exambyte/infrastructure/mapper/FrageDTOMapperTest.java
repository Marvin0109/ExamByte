package exambyte.infrastructure.mapper;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.Frage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FrageDTOMapperTest {

    private final FrageDTOMapper mapper = new FrageDTOMapperImpl();

    @Test
    @DisplayName("Test FrageDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID profFachID = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(fachId)
                .frageText("Frage 1")
                .maxPunkte(10)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .professorUUID(profFachID)
                .examUUID(examFachId)
                .build();

        // Act
        FrageDTO dto = mapper.toDTO(frage);

        // Assert
        assertNull(dto.getId());
        assertEquals(fachId, dto.getFachId());
        assertEquals(profFachID, dto.getProfessorUUID());
        assertEquals(examFachId, dto.getExamUUID());
        assertEquals(QuestionType.MC, QuestionType.valueOf(dto.getType().name()));
        assertEquals(frage.getFrageText(), dto.getFrageText());
        assertEquals(frage.getMaxPunkte(), dto.getMaxPunkte());
    }

    @Test
    @DisplayName("test_null_frage_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toFrageDTOList Test")
    void test_03() {
        // Arrange
        UUID fachId1 = UUID.randomUUID();
        UUID fachId2 = UUID.randomUUID();

        UUID profFachId1 = UUID.randomUUID();
        UUID profFachId2 = UUID.randomUUID();

        UUID examFachId1 = UUID.randomUUID();
        UUID examFachId2 = UUID.randomUUID();

        Frage frage1 = new Frage.FrageBuilder()
                .id(null)
                .fachId(fachId1)
                .frageText("Frage 1")
                .maxPunkte(4)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .professorUUID(profFachId1)
                .examUUID(examFachId1)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .id(null)
                .fachId(fachId2)
                .frageText("Frage 2")
                .maxPunkte(9)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .professorUUID(profFachId2)
                .examUUID(examFachId2)
                .build();

        List<Frage> fragen  = Arrays.asList(frage1, frage2);

        // Act
        List<FrageDTO> frageDTOList = mapper.toFrageDTOList(fragen);

        // Assert
        assertEquals(fragen.size(), frageDTOList.size());
        assertThat(frageDTOList.getFirst().getFachId()).isEqualTo(fachId1);
        assertThat(frageDTOList.getFirst().getType()).isEqualTo(QuestionTypeDTO.valueOf(frage1.getType().name()));
        assertThat(frageDTOList.getFirst().getProfessorUUID()).isEqualTo(profFachId1);
        assertThat(frageDTOList.getFirst().getExamUUID()).isEqualTo(examFachId1);
        assertThat(frageDTOList.getFirst().getFrageText()).isEqualTo(frage1.getFrageText());

        assertThat(frageDTOList.getLast().getFachId()).isEqualTo(fachId2);
        assertThat(frageDTOList.getLast().getProfessorUUID()).isEqualTo(profFachId2);
        assertThat(frageDTOList.getLast().getType()).isEqualTo(QuestionTypeDTO.valueOf(frage2.getType().name()));
        assertThat(frageDTOList.getLast().getExamUUID()).isEqualTo(examFachId2);
        assertThat(frageDTOList.getLast().getFrageText()).isEqualTo(frage2.getFrageText());
    }
}
