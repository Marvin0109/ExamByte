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
        UUID answerId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();

        Review review = new Review.ReviewBuilder()
                .id(id)
                .answerId(answerId)
                .reviewerId(reviewerId)
                .bewertung("Bewertung")
                .punkte(3)
                .build();

        // Act
        ReviewDTO reviewDTO = mapper.toDTO(review);

        // Assert
        assertEquals(id, reviewDTO.id());
        assertEquals(answerId, reviewDTO.answerId());
        assertEquals(reviewerId, reviewDTO.reviewerId());
        assertEquals("Bewertung", reviewDTO.text());
        assertEquals(3, reviewDTO.points());
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
        UUID answerId1 = UUID.randomUUID();
        UUID reviewerId1 = UUID.randomUUID();

        UUID id2 = UUID.randomUUID();
        UUID answerId2 = UUID.randomUUID();
        UUID reviewerId2 = UUID.randomUUID();

        Review review1 = new Review.ReviewBuilder()
                .id(id1)
                .answerId(answerId1)
                .reviewerId(reviewerId1)
                .bewertung("Bewertung 1")
                .punkte(3)
                .build();

        Review review2 = new Review.ReviewBuilder()
                .id(id2)
                .answerId(answerId2)
                .reviewerId(reviewerId2)
                .bewertung("Bewertung 2")
                .punkte(6)
                .build();

        List<Review> reviews = Arrays.asList(review1, review2);

        // Act
        List<ReviewDTO> reviewDTOs = mapper.toReviewDTOList(reviews);

        // Assert
        assertEquals(2, reviewDTOs.size());
        assertThat(reviewDTOs.getFirst().id()).isEqualTo(id1);
        assertThat(reviewDTOs.getFirst().answerId()).isEqualTo(answerId1);
        assertThat(reviewDTOs.getFirst().reviewerId()).isEqualTo(reviewerId1);
        assertThat(reviewDTOs.getFirst().text()).isEqualTo("Bewertung 1");
        assertThat(reviewDTOs.getFirst().points()).isEqualTo(3);

        assertThat(reviewDTOs.getLast().id()).isEqualTo(id2);
        assertThat(reviewDTOs.getLast().answerId()).isEqualTo(answerId2);
        assertThat(reviewDTOs.getLast().reviewerId()).isEqualTo(reviewerId2);
        assertThat(reviewDTOs.getLast().text()).isEqualTo("Bewertung 2");
        assertThat(reviewDTOs.getLast().points()).isEqualTo(6);
    }
}
