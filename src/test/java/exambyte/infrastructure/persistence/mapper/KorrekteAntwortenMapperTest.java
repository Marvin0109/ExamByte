package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.KorrekteAntwortenMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class KorrekteAntwortenMapperTest {

    private KorrekteAntwortenMapper korrekteAntwortenMapper;

    @BeforeEach
    void setUp() {
        korrekteAntwortenMapper = new KorrekteAntwortenMapperImpl();
    }

    @Test
    @DisplayName("toDomain Test")
    void test_01() {
        // Arrange
        UUID fachID = UUID.randomUUID();
        UUID frageFachID = UUID.randomUUID();
        String antworten = "Lösung 1, Lösung 2";
        KorrekteAntwortenEntity entity = new KorrekteAntwortenEntity(null,
                fachID, frageFachID, antworten);

        // Act
        KorrekteAntworten korrekteAntworten = korrekteAntwortenMapper.toDomain(entity);

        // Assert
        assertThat(korrekteAntworten.getFachId()).isEqualTo(fachID);
        assertThat(korrekteAntworten.getFrageFachId()).isEqualTo(frageFachID);
        assertThat(korrekteAntworten.getKorrekteAntworten()).contains("Lösung 1, Lösung 2");
    }

    @Test
    @DisplayName("toEntity Test")
    void test_02() {
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
        KorrekteAntwortenEntity entity = korrekteAntwortenMapper.toEntity(antworten);

        // Assert
        assertThat(entity.getFachID()).isEqualTo(antworten.getFachId());
        assertThat(entity.getFrageFachID()).isEqualTo(antworten.getFrageFachId());
        assertThat(entity.getRichtigeAntwort()).contains(korrekteAntworten);
    }
}
