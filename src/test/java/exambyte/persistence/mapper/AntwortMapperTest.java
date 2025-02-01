package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AntwortMapperTest {

    @Test
    @DisplayName("AntwortMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        Antwort antwort = Antwort.of(
                null,
                fachId,
                "Antworttext",
                frageFachId,
                studentFachId,
                LocalDateTime.now(),
                LocalDateTime.now());

        AntwortMapper antwortMapper = new AntwortMapper();

        // Act
        AntwortEntity antwortEntity = antwortMapper.toEntity(antwort);

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
    public void test_02() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        AntwortEntity antwortEntity = new AntwortEntity(
                null,
                fachId,
                "Antworttext",
                frageFachId,
                studentFachId,
                LocalDateTime.now(),
                LocalDateTime.now());

        AntwortMapper antwortMapper = new AntwortMapper();

        // Act
        Antwort antwort = antwortMapper.toDomain(antwortEntity);

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
