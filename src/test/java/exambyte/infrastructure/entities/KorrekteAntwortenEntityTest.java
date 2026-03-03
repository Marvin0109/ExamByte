package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class KorrekteAntwortenEntityTest {

    @ParameterizedTest
    @DisplayName("Pflichfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createKorrekteAntwortenEntity_fail(KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageId(null)
                        .richtigeAntwort("A\nB\nC")
                        .antwortOptionen("A\nB\nC\nD"),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageId(UUID.randomUUID())
                        .richtigeAntwort("")
                        .antwortOptionen("A\nB\nC\nD"),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageId(UUID.randomUUID())
                        .richtigeAntwort(" ")
                        .antwortOptionen("A\nB\nC\nD"),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageId(UUID.randomUUID())
                        .richtigeAntwort("A\nB\nC")
                        .antwortOptionen(""),

                new KorrekteAntwortenEntity.KorrekteAntwortenEntityBuilder()
                        .frageId(UUID.randomUUID())
                        .richtigeAntwort("A\nB\nC")
                        .antwortOptionen(" ")
        );
    }
}
