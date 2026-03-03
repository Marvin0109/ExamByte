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
        UUID id = UUID.randomUUID();
        UUID antwortId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();

        Review review = new Review.ReviewBuilder()
                .id(id)
                .antwortId(antwortId)
                .korrektorId(korrektorId)
                .bewertung("Bewertung")
                .punkte(3)
                .build();

        // Act
        ReviewDTO reviewDTO = mapper.toDTO(review);

        // Assert
        assertEquals(id, reviewDTO.id());
        assertEquals(antwortId, reviewDTO.antwortId());
        assertEquals(korrektorId, reviewDTO.korrektorId());
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
        UUID id1 = UUID.randomUUID();
        UUID antwortId1 = UUID.randomUUID();
        UUID korrektorId1 = UUID.randomUUID();

        UUID id2 = UUID.randomUUID();
        UUID antwortId2 = UUID.randomUUID();
        UUID korrektorId2 = UUID.randomUUID();

        Review review1 = new Review.ReviewBuilder()
                .id(id1)
                .antwortId(antwortId1)
                .korrektorId(korrektorId1)
                .bewertung("Bewertung 1")
                .punkte(3)
                .build();

        Review review2 = new Review.ReviewBuilder()
                .id(id2)
                .antwortId(antwortId2)
                .korrektorId(korrektorId2)
                .bewertung("Bewertung 2")
                .punkte(6)
                .build();

        List<Review> reviews = Arrays.asList(review1, review2);

        // Act
        List<ReviewDTO> reviewDTOs = mapper.toReviewDTOList(reviews);

        // Assert
        assertEquals(2, reviewDTOs.size());
        assertThat(reviewDTOs.getFirst().id()).isEqualTo(id1);
        assertThat(reviewDTOs.getFirst().antwortId()).isEqualTo(antwortId1);
        assertThat(reviewDTOs.getFirst().korrektorId()).isEqualTo(korrektorId1);
        assertThat(reviewDTOs.getFirst().bewertung()).isEqualTo("Bewertung 1");
        assertThat(reviewDTOs.getFirst().punkte()).isEqualTo(3);

        assertThat(reviewDTOs.getLast().id()).isEqualTo(id2);
        assertThat(reviewDTOs.getLast().antwortId()).isEqualTo(antwortId2);
        assertThat(reviewDTOs.getLast().korrektorId()).isEqualTo(korrektorId2);
        assertThat(reviewDTOs.getLast().bewertung()).isEqualTo("Bewertung 2");
        assertThat(reviewDTOs.getLast().punkte()).isEqualTo(6);
    }
}
