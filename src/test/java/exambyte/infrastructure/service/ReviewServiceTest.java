package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private final ReviewRepository reviewRepository = mock(ReviewRepository.class);
    private final ReviewService reviewService = new ReviewServiceImpl(reviewRepository);

    @Test
    @DisplayName("Eine Bewertung kann gefunden werden mit der Answer-ID")
    void test_01() {
        // Arrange
        UUID answerId = UUID.randomUUID();
        var review = new Review.ReviewBuilder().answerId(answerId).build();

        when(reviewRepository.findByAnswerId(answerId)).thenReturn(review);

        // Act
        Review result = reviewService.getReviewByAnswerId(answerId);

        // Assert
        assertThat(result.getAnswerId()).isEqualTo(answerId);
        verify(reviewRepository).findByAnswerId(answerId);
    }
}
