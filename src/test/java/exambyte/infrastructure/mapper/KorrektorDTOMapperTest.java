package exambyte.infrastructure.mapper;

import exambyte.application.dto.ReviewerDTO;
import exambyte.domain.mapper.ReviewerDTOMapper;
import exambyte.domain.model.aggregate.user.Reviewer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReviewerDTOMapperTest {

    private final ReviewerDTOMapper mapper = new ReviewerDTOMapperImpl();

    @Test
    @DisplayName("Test ReviewerDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();

        Reviewer reviewer = new Reviewer.ReviewerBuilder()
                .id(id)
                .name("Reviewername")
                .build();

        // Act
        ReviewerDTO dto = mapper.toDTO(reviewer);

        // Assert
        assertEquals(id, dto.id());
        assertEquals("Reviewername", dto.name());
    }

    @Test
    @DisplayName("test_null_reviewer_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toReviewerDTOList Test")
    void test_03() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Reviewer reviewer1 = new Reviewer.ReviewerBuilder()
                .id(id1)
                .name("Reviewer 1")
                .build();

        Reviewer reviewer2 = new Reviewer.ReviewerBuilder()
                .id(id2)
                .name("Reviewer 2")
                .build();

        List<Reviewer> reviewers = Arrays.asList(reviewer1, reviewer2);

        // Act
        List<ReviewerDTO> reviewerDTOList = mapper.toReviewerDTOList(reviewers);

        // Assert
        assertEquals(2, reviewerDTOList.size());
        assertThat(reviewerDTOList.getFirst().name()).isEqualTo("Reviewer 1");
        assertThat(reviewerDTOList.getFirst().id()).isEqualTo(id1);
        assertThat(reviewerDTOList.getLast().name()).isEqualTo("Reviewer 2");
        assertThat(reviewerDTOList.getLast().id()).isEqualTo(id2);
    }
}
