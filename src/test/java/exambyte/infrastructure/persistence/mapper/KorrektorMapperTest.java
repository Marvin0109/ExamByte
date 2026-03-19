package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.entitymapper.ReviewerMapper;
import exambyte.infrastructure.persistence.entities.ReviewerEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewerMapperTest {

    private final ReviewerMapper reviewerMapper = new ReviewerMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        Reviewer reviewer = new Reviewer.ReviewerBuilder()
                .name("Reviewer1")
                .build();

        // Act
        ReviewerEntity reviewerEntity = reviewerMapper.toEntity(reviewer);

        // Assert
        assertThat(reviewerEntity.getName()).isEqualTo("Reviewer1");
    }

    @Test
    void toDomain() {
        // Arrange
        ReviewerEntity reviewerEntity = new ReviewerEntity.ReviewerEntityBuilder()
                .name("Reviewer2")
                .build();

        // Act
        Reviewer reviewer = reviewerMapper.toDomain(reviewerEntity);

        // Assert
        assertThat(reviewer.getName()).isEqualTo("Reviewer2");
    }
}
