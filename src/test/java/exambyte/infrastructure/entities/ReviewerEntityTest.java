package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.ReviewerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewerEntityTest {

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createReviewerEntity_fail(ReviewerEntity.ReviewerEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    static Stream<ReviewerEntity.ReviewerEntityBuilder> invalidBuilder() {
        return Stream.of(
                new ReviewerEntity.ReviewerEntityBuilder()
                        .name(" "),
                new ReviewerEntity.ReviewerEntityBuilder()
                        .name("")
        );
    }
}
