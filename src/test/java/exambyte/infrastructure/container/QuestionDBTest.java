package exambyte.infrastructure.container;

import exambyte.domain.model.exam.Question;
import exambyte.infrastructure.mapper.QuestionMapper;
import exambyte.domain.repository.QuestionRepository;
import exambyte.infrastructure.repository.QuestionDAO;
import exambyte.infrastructure.repository.QuestionRepositoryImpl;
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
class QuestionDBTest {

    @Autowired
    private QuestionDAO dao;

    private QuestionRepository repository;

    private static final UUID QUESTION_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    @BeforeEach
    void setUp() {
        QuestionMapper questionMapper = new QuestionMapper();
        repository = new QuestionRepositoryImpl(dao, questionMapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Question> loaded = repository.findById(QUESTION_ID);

        // Assert
        assertThat(loaded).isPresent();
    }
}
