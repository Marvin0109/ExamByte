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
        UUID antwortId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();
        String bewertung = "Good work";
        double punkte = 85;

        Review review = new Review.ReviewBuilder()
                .id(id)
                .antwortId(antwortId)
                .korrektorId(korrektorId)
                .bewertung(bewertung)
                .punkte(punkte)
                .build();

        assertEquals(id, review.getId());
        assertEquals(antwortId, review.getAntwortId());
        assertEquals(korrektorId, review.getKorrektorId());
        assertEquals(bewertung, review.getBewertung());
        assertEquals(punkte, review.getPunkte());
    }
}
