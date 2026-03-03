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
    @DisplayName("Eine Bewertung kann gefunden werden mit der Antwort-ID")
    void test_01() {
        // Arrange
        UUID antwortId = UUID.randomUUID();
        var review = new Review.ReviewBuilder().antwortId(antwortId).build();

        when(reviewRepository.findByAntwortId(antwortId)).thenReturn(review);

        // Act
        Review result = reviewService.getReviewByAntwortId(antwortId);

        // Assert
        assertThat(result.getAntwortId()).isEqualTo(antwortId);
        verify(reviewRepository).findByAntwortId(antwortId);
    }
}
