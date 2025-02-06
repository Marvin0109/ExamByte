package exambyte.domain.aggregate.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class ReviewTest {

    // Create Review object with all valid parameters using builder pattern
    @Test
    @DisplayName("test_create_review_with_valid_parameters")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        UUID antwortFachId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();
        String bewertung = "Good work";
        int punkte = 85;

        Review review = new Review.ReviewBuilder()
                .id(id)
                .fachId(fachId)
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorId)
                .bewertung(bewertung)
                .punkte(punkte)
                .build();

        assertEquals(id, review.getId());
        assertEquals(fachId, review.getFachId());
        assertEquals(antwortFachId, review.getAntwortFachId());
        assertEquals(korrektorId, review.getKorrektorFachId());
        assertEquals(bewertung, review.getBewertung());
        assertEquals(punkte, review.getPunkte());
    }

    // Create Review with null values for optional fields
    @Test
    @DisplayName("test_create_review_with_null_optional_fields")
    public void test_02() {
        Long id = 1L;
        UUID antwortFachId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();

        Review review = new Review.ReviewBuilder()
                .id(id)
                .fachId(null)
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorId)
                .bewertung(null)
                .punkte(0)
                .build();

        assertEquals(id, review.getId());
        assertNotNull(review.getFachId());
        assertEquals(antwortFachId, review.getAntwortFachId());
        assertEquals(korrektorId, review.getKorrektorFachId());
        assertNull(review.getBewertung());
        assertEquals(0, review.getPunkte());
    }
}
