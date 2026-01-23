package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AntwortMapperTest {

    private final AntwortMapper mapper = new AntwortMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeit = LocalDateTime.of(2025, 1, 1, 12, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
            .antwortText("Antworttext")
            .frageFachId(frageFachId)
            .studentFachId(studentFachId)
            .antwortZeitpunkt(antwortZeit)
            .build();

        // Act
        AntwortEntity antwortEntity = mapper.toEntity(antwort);

        // Assert
        assertThat(antwortEntity.getAntwortText()).isEqualTo("Antworttext");
        assertThat(antwortEntity.getFrageFachId()).isEqualTo(frageFachId);
        assertThat(antwortEntity.getStudentFachId()).isEqualTo(studentFachId);
        assertThat(antwortEntity.getAntwortZeitpunkt()).isEqualTo(antwort.getAntwortZeitpunkt());
    }

    @Test
    void toDomain() {
        // Arrange
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeit = LocalDateTime.of(2025, 1, 1, 12, 0);

        AntwortEntity antwortEntity = new AntwortEntity.AntwortEntityBuilder()
            .antwortText("Antworttext")
            .frageFachId(frageFachId)
            .studentFachId(studentFachId)
            .antwortZeitpunkt(antwortZeit)
            .build();

        // Act
        Antwort antwort = mapper.toDomain(antwortEntity);

        // Assert
        assertThat(antwort.getAntwortText()).isEqualTo("Antworttext");
        assertThat(antwort.getFrageFachId()).isEqualTo(frageFachId);
        assertThat(antwort.getStudentUUID()).isEqualTo(studentFachId);
        assertThat(antwort.getAntwortZeitpunkt()).isEqualTo(antwortEntity.getAntwortZeitpunkt());
    }
}
