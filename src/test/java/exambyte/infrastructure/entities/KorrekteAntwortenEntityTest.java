package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KorrekteAntwortenEntityTest {

    @Test
    @DisplayName("Die KorrekteAntworten-Fach-ID wird immer generiert")
    void createKorrekteAntworten_success() {
        KorrekteAntwortenEntity korrekteAntwortenEntity = new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                .frageFachID(UUID.randomUUID())
                .richtigeAntwort("A\nB\nC")
                .antwortOptionen("A\nB\nC\nD")
                .build();

        assertThat(korrekteAntwortenEntity.getFachID()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Pflichfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createKorrekteAntwortenEntity_fail(KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageFachID(null)
                        .richtigeAntwort("A\nB\nC")
                        .antwortOptionen("A\nB\nC\nD"),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageFachID(UUID.randomUUID())
                        .richtigeAntwort("")
                        .antwortOptionen("A\nB\nC\nD"),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageFachID(UUID.randomUUID())
                        .richtigeAntwort(" ")
                        .antwortOptionen("A\nB\nC\nD"),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageFachID(UUID.randomUUID())
                        .richtigeAntwort("A\nB\nC")
                        .antwortOptionen(""),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageFachID(UUID.randomUUID())
                        .richtigeAntwort("A\nB\nC")
                        .antwortOptionen(" ")
        );
    }
}
