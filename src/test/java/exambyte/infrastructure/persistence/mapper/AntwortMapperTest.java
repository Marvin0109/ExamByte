package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AntwortMapperTest {

    private final AntwortMapper mapper = new AntwortMapperImpl();

    @Test
    @DisplayName("AntwortMapper test 'toEntity'")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeit = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime lastChangesZeit = LocalDateTime.of(2025, 1, 1, 12, 5);
        Antwort antwort = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(fachId)
                .antwortText("Antworttext")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeit)
                .lastChangesZeitpunkt(lastChangesZeit)
                .build();

        // Act
        AntwortEntity antwortEntity = mapper.toEntity(antwort);

        // Assert
        assertThat(antwortEntity).isNotNull();
        assertThat(antwortEntity.getFachId()).isEqualTo(fachId);
        assertThat(antwortEntity.getAntwortText()).isEqualTo("Antworttext");
        assertThat(antwortEntity.getFrageFachId()).isEqualTo(frageFachId);
        assertThat(antwortEntity.getStudentFachId()).isEqualTo(studentFachId);
        assertThat(antwortEntity.getAntwortZeitpunkt()).isEqualTo(antwort.getAntwortZeitpunkt());
        assertThat(antwortEntity.getLastChangesZeitpunkt()).isEqualTo(antwort.getLastChangesZeitpunkt());
    }

    @Test
    @DisplayName("AntwortMapper test 'toDomain'")
    void test_02() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeit = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime lastChangesZeit = LocalDateTime.of(2025, 1, 1, 12, 5);

        AntwortEntity antwortEntity = new AntwortEntity.AntwortEntityBuilder()
                .id(null)
                .fachId(fachId)
                .antwortText("Antworttext")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeit)
                .lastChangesZeitpunkt(lastChangesZeit)
                .build();

        // Act
        Antwort antwort = mapper.toDomain(antwortEntity);

        // Assert
        assertThat(antwort).isNotNull();
        assertThat(antwort.getFachId()).isEqualTo(fachId);
        assertThat(antwort.getAntwortText()).isEqualTo("Antworttext");
        assertThat(antwort.getFrageFachId()).isEqualTo(frageFachId);
        assertThat(antwort.getStudentUUID()).isEqualTo(studentFachId);
        assertThat(antwort.getAntwortZeitpunkt()).isEqualTo(antwortEntity.getAntwortZeitpunkt());
        assertThat(antwort.getLastChangesZeitpunkt()).isEqualTo(antwortEntity.getLastChangesZeitpunkt());
    }
}
