package exambyte.infrastructure.repository;

import exambyte.application.mapper.export.mapper.ReviewMapper;
import exambyte.domain.model.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.infrastructure.entity.ReviewEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReviewRepositoryTest {

    private final ReviewDAO dao = mock(ReviewDAO.class);
    private final ReviewMapper mapper = mock(ReviewMapper.class);

    private ReviewRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ReviewRepositoryImpl(dao, mapper);
    }

    @Test
    void findByAnswerId_exists() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReviewEntity reviewEntity = new ReviewEntity.ReviewEntityBuilder()
                .text("B")
                .answerId(UUID.randomUUID())
                .reviewerId(UUID.randomUUID())
                .points(1)
                .build();

        Review review = new Review.ReviewBuilder()
                .text("B")
                .id(reviewEntity.getId())
                .answerId(reviewEntity.getAnswerId())
                .reviewerId(reviewEntity.getReviewerId())
                .points(1)
                .build();

        when(dao.findByAnswerId(id)).thenReturn(Optional.of(reviewEntity));
        when(mapper.toDomain(reviewEntity)).thenReturn(review);

        // Act
        Review result = repository.findByAnswerId(id);

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void findByAnswerId_notFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(dao.findByAnswerId(id)).thenReturn(Optional.empty());

        // Act
        Review result = repository.findByAnswerId(id);

        // Assert
        assertThat(result).isNull();
    }
}
