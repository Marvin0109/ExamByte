package exambyte.domain.exam;

import exambyte.domain.model.aggregate.exam.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewTest {

    @Test
    @DisplayName("ReviewBuilder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        UUID answerId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();
        String bewertung = "Good work";
        double punkte = 85;

        Review review = new Review.ReviewBuilder()
                .id(id)
                .answerId(answerId)
                .reviewerId(reviewerId)
                .bewertung(bewertung)
                .punkte(punkte)
                .build();

        assertEquals(id, review.getId());
        assertEquals(answerId, review.getAnswerId());
        assertEquals(reviewerId, review.getReviewerId());
        assertEquals(bewertung, review.getBewertung());
        assertEquals(punkte, review.getPunkte());
    }
}
