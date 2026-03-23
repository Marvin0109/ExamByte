package exambyte.infrastructure.service;

import exambyte.domain.model.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.domain.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private final ReviewRepository repository = mock(ReviewRepository.class);
    private final ReviewService service = new ReviewServiceImpl(repository);

    @Test
    void findByAnswerId_success() {
        // Arrange
        UUID answerId = UUID.randomUUID();
        var review = new Review.ReviewBuilder().answerId(answerId).build();

        when(repository.findByAnswerId(answerId)).thenReturn(review);

        // Act
        Review result = service.getReviewByAnswerId(answerId);

        // Assert
        assertThat(result.getAnswerId()).isEqualTo(answerId);
        verify(repository).findByAnswerId(answerId);
    }
}
