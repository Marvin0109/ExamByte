package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.ReviewMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewMapperTest {

    private final ReviewMapper reviewMapper = new ReviewMapperImpl();
    private static final UUID REVIEW_ID = UUID.randomUUID();
    private static final UUID ANTWORT_ID = UUID.randomUUID();
    private static final UUID KORREKTOR_ID = UUID.randomUUID();

    @Test
    void toEntity() {
        // Arrange
        Review review = new Review.ReviewBuilder()
                .id(REVIEW_ID)
                .antwortId(ANTWORT_ID)
                .korrektorId(KORREKTOR_ID)
                .bewertung("Bewertung")
                .punkte(0.5)
                .build();

        // Act
        ReviewEntity result = reviewMapper.toEntity(review);

        // Assert
        assertThat(result.getId()).isEqualTo(REVIEW_ID);
        assertThat(result.getAntwortId()).isEqualTo(ANTWORT_ID);
        assertThat(result.getKorrektorId()).isEqualTo(KORREKTOR_ID);
        assertThat(result.getBewertung()).isEqualTo("Bewertung");
        assertThat(result.getPunkte()).isEqualTo(1);
    }

    @Test
    void toDomain() {
        // Arrange
        ReviewEntity entity = new ReviewEntity.ReviewEntityBuilder()
                .id(REVIEW_ID)
                .antwortId(ANTWORT_ID)
                .korrektorId(KORREKTOR_ID)
                .bewertung("Bewertung")
                .punkte(13)
                .build();

        // Act
        Review result = reviewMapper.toDomain(entity);

        // Assert
        assertThat(result.getId()).isEqualTo(REVIEW_ID);
        assertThat(result.getAntwortId()).isEqualTo(ANTWORT_ID);
        assertThat(result.getKorrektorId()).isEqualTo(KORREKTOR_ID);
        assertThat(result.getBewertung()).isEqualTo("Bewertung");
        assertThat(result.getPunkte()).isCloseTo(6.5, Offset.offset(0.001));
    }
}
