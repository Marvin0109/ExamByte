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
        UUID frageId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime antwortZeit = LocalDateTime.of(2025, 1, 1, 12, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
            .antwortText("Antworttext")
            .frageId(frageId)
            .studentId(studentId)
            .antwortZeitpunkt(antwortZeit)
            .build();

        // Act
        AntwortEntity antwortEntity = mapper.toEntity(antwort);

        // Assert
        assertThat(antwortEntity.getAntwortText()).isEqualTo("Antworttext");
        assertThat(antwortEntity.getFrageId()).isEqualTo(frageId);
        assertThat(antwortEntity.getStudentId()).isEqualTo(studentId);
        assertThat(antwortEntity.getAntwortZeitpunkt()).isEqualTo(antwort.getAntwortZeitpunkt());
    }

    @Test
    void toDomain() {
        // Arrange
        UUID frageId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime antwortZeit = LocalDateTime.of(2025, 1, 1, 12, 0);

        AntwortEntity antwortEntity = new AntwortEntity.AntwortEntityBuilder()
            .antwortText("Antworttext")
            .frageId(frageId)
            .studentId(studentId)
            .antwortZeitpunkt(antwortZeit)
            .build();

        // Act
        Antwort antwort = mapper.toDomain(antwortEntity);

        // Assert
        assertThat(antwort.getAntwortText()).isEqualTo("Antworttext");
        assertThat(antwort.getFrageId()).isEqualTo(frageId);
        assertThat(antwort.getStudentUUID()).isEqualTo(studentId);
        assertThat(antwort.getAntwortZeitpunkt()).isEqualTo(antwortEntity.getAntwortZeitpunkt());
    }
}
