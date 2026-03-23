package exambyte.infrastructure.persistence.mapper;

import exambyte.infrastructure.mapper.ReviewMapper;
import exambyte.domain.model.exam.Review;
import exambyte.infrastructure.mapper.ReviewMapperImpl;
import exambyte.infrastructure.entity.ReviewEntity;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewMapperTest {

    private final ReviewMapper mapper = new ReviewMapperImpl();
    private static final UUID REVIEW_ID = UUID.randomUUID();
    private static final UUID ANSWER_ID = UUID.randomUUID();
    private static final UUID REVIEWER_ID = UUID.randomUUID();

    @Test
    void toEntity() {
        // Arrange
        Review review = new Review.ReviewBuilder()
                .id(REVIEW_ID)
                .answerId(ANSWER_ID)
                .reviewerId(REVIEWER_ID)
                .text("Text")
                .points(0.5)
                .build();

        // Act
        ReviewEntity result = mapper.toEntity(review);

        // Assert
        assertThat(result.getId()).isEqualTo(REVIEW_ID);
        assertThat(result.getAnswerId()).isEqualTo(ANSWER_ID);
        assertThat(result.getReviewerId()).isEqualTo(REVIEWER_ID);
        assertThat(result.getText()).isEqualTo("Text");
        assertThat(result.getPoints()).isEqualTo(1);
    }

    @Test
    void toDomain() {
        // Arrange
        ReviewEntity entity = new ReviewEntity.ReviewEntityBuilder()
                .id(REVIEW_ID)
                .answerId(ANSWER_ID)
                .reviewerId(REVIEWER_ID)
                .text("Text")
                .points(13)
                .build();

        // Act
        Review result = mapper.toDomain(entity);

        // Assert
        assertThat(result.getId()).isEqualTo(REVIEW_ID);
        assertThat(result.getAnswerId()).isEqualTo(ANSWER_ID);
        assertThat(result.getReviewerId()).isEqualTo(REVIEWER_ID);
        assertThat(result.getText()).isEqualTo("Text");
        assertThat(result.getPoints()).isCloseTo(6.5, Offset.offset(0.001));
    }
}
