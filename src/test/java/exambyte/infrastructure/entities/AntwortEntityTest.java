package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AntwortEntityTest {

    @Test
    @DisplayName("Die Antwort-Fach-ID wird immer generiert")
    void createAntwortEntity_success() {
        AntwortEntity antwortEntity = new AntwortEntity.AntwortEntityBuilder()
                .studentFachId(UUID.randomUUID())
                .frageFachId(UUID.randomUUID())
                .antwortText("Text")
                .build();

        assertThat(antwortEntity.getFachId()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createAntwortEntity_fail(AntwortEntity.AntwortEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<AntwortEntity.AntwortEntityBuilder> ungueltigeBuilder() {
        UUID studentFachId = UUID.randomUUID();

        return Stream.of(
                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText(null)
                        .frageFachId(UUID.randomUUID())
                        .studentFachId(studentFachId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText("")
                        .frageFachId(UUID.randomUUID())
                        .studentFachId(studentFachId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText(" ")
                        .frageFachId(UUID.randomUUID())
                        .studentFachId(studentFachId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText("Antwort")
                        .frageFachId(null)
                        .studentFachId(studentFachId),

                new AntwortEntity.AntwortEntityBuilder()
                        .antwortText("Antwort")
                        .frageFachId(UUID.randomUUID())
                        .studentFachId(null)
        );
    }

}
