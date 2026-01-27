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
    void findByAntwortFachId_exists() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        ReviewEntity reviewEntity = new ReviewEntity.ReviewEntityBuilder()
                .bewertung("B")
                .antwortFachId(UUID.randomUUID())
                .korrektorFachId(UUID.randomUUID())
                .punkte(1)
                .build();

        Review review = new Review.ReviewBuilder()
                .bewertung("B")
                .fachId(reviewEntity.getFachId())
                .antwortFachId(reviewEntity.getAntwortFachId())
                .korrektorFachId(reviewEntity.getKorrektorFachId())
                .punkte(1)
                .build();

        when(reviewDAO.findByAntwortFachId(fachId)).thenReturn(Optional.of(reviewEntity));
        when(reviewMapper.toDomain(reviewEntity)).thenReturn(review);

        // Act
        Review result = repository.findByAntwortFachId(fachId);

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void findByAntwortFachId_notFound() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        when(reviewDAO.findByAntwortFachId(fachId)).thenReturn(Optional.empty());

        // Act
        Review result = repository.findByAntwortFachId(fachId);

        // Assert
        assertThat(result).isNull();
    }
}
