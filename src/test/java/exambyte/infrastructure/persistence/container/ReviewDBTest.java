package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.ReviewMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.infrastructure.persistence.mapper.ReviewMapperImpl;
import exambyte.infrastructure.persistence.repository.ReviewDAO;
import exambyte.infrastructure.persistence.repository.ReviewRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql(scripts = "/data-test.sql")
class ReviewDBTest {

    @Autowired
    private ReviewDAO reviewDAO;

    private ReviewRepository reviewRepository;

    private static final UUID REVIEWUUID = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");

    @BeforeEach
    void setUp() {
        ReviewMapper reviewMapper = new ReviewMapperImpl();
        reviewRepository = new ReviewRepositoryImpl(reviewDAO, reviewMapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<Review> geladenReview = reviewRepository.findByFachId(REVIEWUUID);

        // Assert
        assertThat(geladenReview).isPresent();
    }
}
