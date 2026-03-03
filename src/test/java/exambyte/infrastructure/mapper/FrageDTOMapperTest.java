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
        UUID id = UUID.randomUUID();
        UUID examId = UUID.randomUUID();

        Frage frage = new Frage.FrageBuilder()
                .id(id)
                .frageText("Frage 1")
                .maxPunkte(10)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .examId(examId)
                .build();

        // Act
        FrageDTO dto = mapper.toDTO(frage);

        // Assert
        assertEquals(id, dto.id());
        assertEquals(examId, dto.examId());
        assertEquals(QuestionType.MC, QuestionType.valueOf(dto.type().name()));
        assertEquals(frage.getFrageText(), dto.frageText());
        assertEquals(frage.getMaxPunkte(), dto.maxPunkte());
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
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        UUID examId1 = UUID.randomUUID();
        UUID examId2 = UUID.randomUUID();

        Frage frage1 = new Frage.FrageBuilder()
                .id(id1)
                .frageText("Frage 1")
                .maxPunkte(4)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .examId(examId1)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .id(id2)
                .frageText("Frage 2")
                .maxPunkte(9)
                .type(QuestionType.valueOf(QuestionTypeDTO.MC.name()))
                .examId(examId2)
                .build();

        List<Frage> fragen  = Arrays.asList(frage1, frage2);

        // Act
        List<FrageDTO> frageDTOList = mapper.toFrageDTOList(fragen);

        // Assert
        assertEquals(fragen.size(), frageDTOList.size());
        assertThat(frageDTOList.getFirst().id()).isEqualTo(id1);
        assertThat(frageDTOList.getFirst().type()).isEqualTo(QuestionTypeDTO.valueOf(frage1.getType().name()));
        assertThat(frageDTOList.getFirst().examId()).isEqualTo(examId1);
        assertThat(frageDTOList.getFirst().frageText()).isEqualTo(frage1.getFrageText());

        assertThat(frageDTOList.getLast().id()).isEqualTo(id2);
        assertThat(frageDTOList.getLast().type()).isEqualTo(QuestionTypeDTO.valueOf(frage2.getType().name()));
        assertThat(frageDTOList.getLast().examId()).isEqualTo(examId2);
        assertThat(frageDTOList.getLast().frageText()).isEqualTo(frage2.getFrageText());
    }
}
