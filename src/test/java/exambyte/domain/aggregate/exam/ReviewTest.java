package exambyte.domain.aggregate.exam;

import exambyte.domain.model.aggregate.exam.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewTest {

    @Test
    @DisplayName("ReviewBuilder Test")
    void test_01() {
        UUID fachId = UUID.randomUUID();
        UUID antwortFachId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();
        String bewertung = "Good work";
        int punkte = 85;

        Review review = new Review.ReviewBuilder()
                .fachId(fachId)
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorId)
                .bewertung(bewertung)
                .punkte(punkte)
                .build();

        assertEquals(fachId, review.getFachId());
        assertEquals(antwortFachId, review.getAntwortFachId());
        assertEquals(korrektorId, review.getKorrektorFachId());
        assertEquals(bewertung, review.getBewertung());
        assertEquals(punkte, review.getPunkte());
    }
}
