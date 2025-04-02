package exambyte.infrastructure.mapper;

import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class KorrekteAntwortDTOMapperTest {

    private KorrekteAntwortenDTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new KorrekteAntwortenDTOMapperImpl();
    }

    @Test
    @DisplayName("toDTO Test")
    void test_01() {
        // Arrange
        UUID fachID = UUID.randomUUID();
        UUID frageFachID = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1, Lösung 2";

        KorrekteAntworten antworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(fachID)
                .frageFachId(frageFachID)
                .korrekteAntworten(korrekteAntworten)
                .build();

        // Act
        KorrekteAntwortenDTO dto = mapper.toDTO(antworten);

        // Assert
        assertThat(dto.getFachID()).isEqualTo(fachID);
        assertThat(dto.getFrageFachID()).isEqualTo(frageFachID);
        assertThat(dto.getAntworten()).contains(korrekteAntworten);
    }

    @Test
    @DisplayName("toDomain Test")
    void test_02() {
        // Arrange
        UUID fachID = UUID.randomUUID();
        UUID frageFachID = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1, Lösung 2";

        KorrekteAntwortenDTO dto = new KorrekteAntwortenDTO(null, fachID, frageFachID, korrekteAntworten);

        // Act
        KorrekteAntworten result = mapper.toDomain(dto);

        // Assert
        assertThat(result.getFachId()).isEqualTo(fachID);
        assertThat(result.getFrageFachId()).isEqualTo(frageFachID);
        assertThat(result.getKorrekteAntworten()).contains(korrekteAntworten);
    }
}
