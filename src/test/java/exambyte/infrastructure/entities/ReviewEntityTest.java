package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewEntityTest {

    @Test
    @DisplayName("ReviewEntity-Fach-ID wird immer generiert")
    void createReviewEntity_success() {
        ReviewEntity reviewEntity = new ReviewEntity.ReviewEntityBuilder()
                .bewertung("Bewertung")
                .antwortFachId(UUID.randomUUID())
                .korrektorFachId(UUID.randomUUID())
                .punkte(1)
                .build();

        assertThat(reviewEntity.getFachId()).isNotNull();
    }

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
                        .antwortFachId(UUID.randomUUID())
                        .korrektorFachId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung(" ")
                        .antwortFachId(UUID.randomUUID())
                        .korrektorFachId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .antwortFachId(null)
                        .korrektorFachId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .antwortFachId(UUID.randomUUID())
                        .korrektorFachId(null)
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .antwortFachId(UUID.randomUUID())
                        .korrektorFachId(UUID.randomUUID())
                        .punkte(-1)
        );
    }
}
