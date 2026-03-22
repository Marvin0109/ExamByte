package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.ReviewerMapper;
import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.repository.ReviewerRepository;
import exambyte.infrastructure.persistence.mapper.ReviewerMapperImpl;
import exambyte.infrastructure.persistence.repository.ReviewerDAO;
import exambyte.infrastructure.persistence.repository.ReviewerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql("/data-test.sql")
class ReviewerDBTest {

    @Autowired
    private ReviewerDAO dao;

    private ReviewerRepository repository;

    private static final UUID REVIEWER_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @BeforeEach
    void setUp() {
        ReviewerMapper mapper = new ReviewerMapperImpl();
        repository = new ReviewerRepositoryImpl(dao, mapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Reviewer> loaded = repository.findById(REVIEWER_ID);

        // Assert
        assertThat(loaded).isPresent();
    }
}
