package exambyte.infrastructure.mapper;

import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class KorrekteAntwortDTOMapperTest {

    private KorrekteAntwortenDTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new KorrekteAntwortenDTOMapperImpl();
    }

    @Test
    @DisplayName("toDTO Test")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1\nLösung 2";
        String antwortOptionen = "Lösung 1\nLösung 2\nLösung 3";

        KorrekteAntworten antworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(id)
                .frageId(frageId)
                .loesungen(korrekteAntworten)
                .antwortOptionen(antwortOptionen)
                .build();

        // Act
        KorrekteAntwortenDTO dto = mapper.toDTO(antworten);

        // Assert
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.frageId()).isEqualTo(frageId);
        assertThat(dto.antworten()).contains(korrekteAntworten);
        assertThat(dto.antwortOptionen()).contains(antwortOptionen);
    }

    @Test
    @DisplayName("toDomain Test")
    void test_02() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1\nLösung 2";
        String antwortOptionen = "Lösung 1\nLösung 2\nLösung 3";

        KorrekteAntwortenDTO dto = new KorrekteAntwortenDTO(
                id,
                korrekteAntworten,
                antwortOptionen,
                frageId);

        // Act
        KorrekteAntworten result = mapper.toDomain(dto);

        // Assert
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFrageId()).isEqualTo(frageId);
        assertThat(result.getLoesungen()).contains(korrekteAntworten);
        assertThat(result.getAntwortOptionen()).contains(antwortOptionen);
    }
}
