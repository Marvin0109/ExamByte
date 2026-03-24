package exambyte.domain.exam;

import exambyte.domain.model.exam.Review;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewTest {

    @Test
    void review_builder_test() {
        UUID id = UUID.randomUUID();
        UUID answerId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();
        String text = "Good work";
        double points = 85;

        Review review = new Review.ReviewBuilder()
                .id(id)
                .answerId(answerId)
                .reviewerId(reviewerId)
                .text(text)
                .points(points)
                .build();

        assertEquals(id, review.getId());
        assertEquals(answerId, review.getAnswerId());
        assertEquals(reviewerId, review.getReviewerId());
        assertEquals(text, review.getText());
        assertEquals(points, review.getPoints());
    }
}
