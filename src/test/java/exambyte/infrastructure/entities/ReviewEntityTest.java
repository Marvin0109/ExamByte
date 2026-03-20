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
    @MethodSource("invalidBuilder")
    void createReviewEntity_fail(ReviewEntity.ReviewEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ReviewEntity.ReviewEntityBuilder> invalidBuilder() {
        return Stream.of(
                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("")
                        .answerId(UUID.randomUUID())
                        .reviewerId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung(" ")
                        .answerId(UUID.randomUUID())
                        .reviewerId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .answerId(null)
                        .reviewerId(UUID.randomUUID())
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .answerId(UUID.randomUUID())
                        .reviewerId(null)
                        .punkte(1),

                new ReviewEntity.ReviewEntityBuilder()
                        .bewertung("Bewertung")
                        .answerId(UUID.randomUUID())
                        .reviewerId(UUID.randomUUID())
                        .punkte(-1)
        );
    }
}
