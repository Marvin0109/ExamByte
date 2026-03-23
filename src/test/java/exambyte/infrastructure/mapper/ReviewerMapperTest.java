package exambyte.infrastructure.mapper;

import exambyte.domain.model.user.Reviewer;
import exambyte.application.mapper.export.mapper.ReviewerMapper;
import exambyte.application.mapper.export.mapper.ReviewerMapperImpl;
import exambyte.infrastructure.entity.ReviewerEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewerMapperTest {

    private final ReviewerMapper mapper = new ReviewerMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        Reviewer reviewer = new Reviewer.ReviewerBuilder()
                .name("Reviewer1")
                .build();

        // Act
        ReviewerEntity entity = mapper.toEntity(reviewer);

        // Assert
        assertThat(entity.getName()).isEqualTo("Reviewer1");
    }

    @Test
    void toDomain() {
        // Arrange
        ReviewerEntity entity = new ReviewerEntity.ReviewerEntityBuilder()
                .name("Reviewer2")
                .build();

        // Act
        Reviewer reviewer = mapper.toDomain(entity);

        // Assert
        assertThat(reviewer.getName()).isEqualTo("Reviewer2");
    }
}
