package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.KorrekteAntwortenMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class KorrekteAntwortenMapperTest {

    private KorrekteAntwortenMapper korrekteAntwortenMapper;

    @BeforeEach
    void setUp() {
        korrekteAntwortenMapper = new KorrekteAntwortenMapperImpl();
    }

    @Test
    void toEntity() {
        // Arrange
        UUID frageFachID = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1\nLösung 2";
        String antwortOptionen = "Lösung 1\nLösung 2\nLösung 3";

        KorrekteAntworten antworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
            .frageFachId(frageFachID)
            .loesungen(korrekteAntworten)
            .antwortOptionen(antwortOptionen)
            .build();

        // Act
        KorrekteAntwortenEntity entity = korrekteAntwortenMapper.toEntity(antworten);

        // Assert
        assertThat(entity.getFrageFachID()).isEqualTo(antworten.getFrageFachId());
        assertThat(entity.getRichtigeAntwort()).contains(korrekteAntworten);
        assertThat(entity.getAntwortOptionen()).contains(antwortOptionen);
    }

    @Test
    void toDomain() {
        // Arrange
        UUID frageFachID = UUID.randomUUID();
        String antworten = "Lösung 1\nLösung 2";
        String antwortOptionen = "Lösung 1\nLösung 2\nLösung 3";
        KorrekteAntwortenEntity entity = new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                .frageFachID(frageFachID)
                .antwortOptionen(antwortOptionen)
                .richtigeAntwort(antworten).build();

        // Act
        KorrekteAntworten korrekteAntworten = korrekteAntwortenMapper.toDomain(entity);

        // Assert
        assertThat(korrekteAntworten.getFrageFachId()).isEqualTo(frageFachID);
        assertThat(korrekteAntworten.getLoesungen()).contains(antworten);
        assertThat(korrekteAntworten.getAntwortOptionen()).contains(antwortOptionen);
    }
}
