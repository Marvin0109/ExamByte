package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    private final ReviewRepository reviewRepository = mock(ReviewRepository.class);
    private final ReviewService reviewService = new ReviewServiceImpl(reviewRepository);

    @Test
    @DisplayName("Eine Bewertung kann hinzugefügt werden")
    void test_01() {
        // Arrange
        Review review = new Review.ReviewBuilder().build();

        // Act
        reviewRepository.save(review);

        // Assert
        verify(reviewRepository).save(review);
    }

    @Test
    @DisplayName("Eine Bewertung kann gefunden werden mit der Antwort-FachID")
    void test_02() {
        // Arrange
        UUID antwortFachId = UUID.randomUUID();
        var review = new Review.ReviewBuilder().antwortFachId(antwortFachId).build();

        when(reviewRepository.findByAntwortFachId(antwortFachId)).thenReturn(review);

        // Act
        Review result = reviewService.getReviewByAntwortFachId(antwortFachId);

        // Assert
        assertThat(result.getAntwortFachId()).isEqualTo(antwortFachId);
        verify(reviewRepository).findByAntwortFachId(antwortFachId);
    }
}
