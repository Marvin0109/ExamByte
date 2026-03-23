package exambyte.infrastructure.container;

import exambyte.application.mapper.export.mapper.ReviewMapper;
import exambyte.domain.model.exam.Review;
import exambyte.domain.repository.ReviewRepository;
import exambyte.application.mapper.export.mapper.ReviewMapperImpl;
import exambyte.infrastructure.repository.ReviewDAO;
import exambyte.infrastructure.repository.ReviewRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
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
    private ReviewDAO dao;

    private ReviewRepository repository;

    private static final UUID REVIEW_ID = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");

    @BeforeEach
    void setUp() {
        ReviewMapper reviewMapper = new ReviewMapperImpl();
        repository = new ReviewRepositoryImpl(dao, reviewMapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Review> loaded = repository.findById(REVIEW_ID);

        // Assert
        assertThat(loaded).isPresent();
    }
}
