package exambyte.infrastructure.persistence.container;

import exambyte.infrastructure.mapper.CorrectAnswersMapper;
import exambyte.domain.model.exam.CorrectAnswers;
import exambyte.domain.repository.CorrectAnswersRepository;
import exambyte.infrastructure.mapper.CorrectAnswersMapperImpl;
import exambyte.infrastructure.repository.CorrectAnswersDAO;
import exambyte.infrastructure.repository.CorrectAnswersRepositoryImpl;
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
class CorrectAnswersDBTest {

    @Autowired
    private CorrectAnswersDAO dao;

    private CorrectAnswersRepository repository;

    private static final UUID CORRECT_ANSWERS_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    @BeforeEach
    void setUp() {
        CorrectAnswersMapper mapper = new CorrectAnswersMapperImpl();
        repository = new CorrectAnswersRepositoryImpl(dao, mapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<CorrectAnswers> loaded = repository.findById(CORRECT_ANSWERS_ID);

        // Assert
        assertThat(loaded).isPresent();
    }
}
