package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.ReviewMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReviewRepositoryTest {

    private final ReviewDAO reviewDAO = mock(ReviewDAO.class);
    private final ReviewMapper reviewMapper = mock(ReviewMapper.class);

    private ReviewRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ReviewRepositoryImpl(reviewDAO, reviewMapper);
    }

    @Test
    void findByAntwortId_exists() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReviewEntity reviewEntity = new ReviewEntity.ReviewEntityBuilder()
                .bewertung("B")
                .antwortId(UUID.randomUUID())
                .korrektorId(UUID.randomUUID())
                .punkte(1)
                .build();

        Review review = new Review.ReviewBuilder()
                .bewertung("B")
                .id(reviewEntity.getId())
                .antwortId(reviewEntity.getAntwortId())
                .korrektorId(reviewEntity.getKorrektorId())
                .punkte(1)
                .build();

        when(reviewDAO.findByAntwortId(id)).thenReturn(Optional.of(reviewEntity));
        when(reviewMapper.toDomain(reviewEntity)).thenReturn(review);

        // Act
        Review result = repository.findByAntwortId(id);

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void findByAntwortId_notFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(reviewDAO.findByAntwortId(id)).thenReturn(Optional.empty());

        // Act
        Review result = repository.findByAntwortId(id);

        // Assert
        assertThat(result).isNull();
    }
}
