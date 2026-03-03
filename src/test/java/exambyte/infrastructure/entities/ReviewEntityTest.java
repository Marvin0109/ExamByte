package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewEntityTest {

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createReviewEntity_fail(ReviewEntity.ReviewEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ReviewEntity.ReviewEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("")
                        .antwortId(UUID.randomUUID())
                        .korrektorId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung(" ")
                        .antwortId(UUID.randomUUID())
                        .korrektorId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .antwortId(null)
                        .korrektorId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .antwortId(UUID.randomUUID())
                        .korrektorId(null)
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .antwortId(UUID.randomUUID())
                        .korrektorId(UUID.randomUUID())
                        .punkte(-1)
        );
    }
}
