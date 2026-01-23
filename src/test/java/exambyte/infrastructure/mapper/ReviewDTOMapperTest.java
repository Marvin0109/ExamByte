package exambyte.infrastructure.mapper;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.model.aggregate.exam.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReviewDTOMapperTest {

    private final ReviewDTOMapper mapper = new ReviewDTOMapperImpl();

    @Test
    @DisplayName("Test ReviewDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID antwortFachId = UUID.randomUUID();
        UUID korrektorFachId = UUID.randomUUID();

        Review review = new Review.ReviewBuilder()
                .fachId(fachId)
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorFachId)
                .bewertung("Bewertung")
                .punkte(3)
                .build();

        // Act
        ReviewDTO reviewDTO = mapper.toDTO(review);

        // Assert
        assertEquals(fachId, reviewDTO.fachId());
        assertEquals(antwortFachId, reviewDTO.antwortFachId());
        assertEquals(korrektorFachId, reviewDTO.korrektorFachId());
        assertEquals("Bewertung", reviewDTO.bewertung());
        assertEquals(3, reviewDTO.punkte());
    }

    @Test
    @DisplayName("test_null_review_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toReviewDTOList Test")
    void test_03() {
        // Arrange
        UUID fachId1 = UUID.randomUUID();
        UUID antwortFachId1 = UUID.randomUUID();
        UUID korrektorFachId1 = UUID.randomUUID();

        UUID fachId2 = UUID.randomUUID();
        UUID antwortFachId2 = UUID.randomUUID();
        UUID korrektorFachId2 = UUID.randomUUID();

        Review review1 = new Review.ReviewBuilder()
                .fachId(fachId1)
                .antwortFachId(antwortFachId1)
                .korrektorFachId(korrektorFachId1)
                .bewertung("Bewertung 1")
                .punkte(3)
                .build();

        Review review2 = new Review.ReviewBuilder()
                .fachId(fachId2)
                .antwortFachId(antwortFachId2)
                .korrektorFachId(korrektorFachId2)
                .bewertung("Bewertung 2")
                .punkte(6)
                .build();

        List<Review> reviews = Arrays.asList(review1, review2);

        // Act
        List<ReviewDTO> reviewDTOs = mapper.toReviewDTOList(reviews);

        // Assert
        assertEquals(2, reviewDTOs.size());
        assertThat(reviewDTOs.getFirst().fachId()).isEqualTo(fachId1);
        assertThat(reviewDTOs.getFirst().antwortFachId()).isEqualTo(antwortFachId1);
        assertThat(reviewDTOs.getFirst().korrektorFachId()).isEqualTo(korrektorFachId1);
        assertThat(reviewDTOs.getFirst().bewertung()).isEqualTo("Bewertung 1");
        assertThat(reviewDTOs.getFirst().punkte()).isEqualTo(3);

        assertThat(reviewDTOs.getLast().fachId()).isEqualTo(fachId2);
        assertThat(reviewDTOs.getLast().antwortFachId()).isEqualTo(antwortFachId2);
        assertThat(reviewDTOs.getLast().korrektorFachId()).isEqualTo(korrektorFachId2);
        assertThat(reviewDTOs.getLast().bewertung()).isEqualTo("Bewertung 2");
        assertThat(reviewDTOs.getLast().punkte()).isEqualTo(6);
    }
}
