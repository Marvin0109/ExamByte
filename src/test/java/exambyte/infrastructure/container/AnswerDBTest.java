package exambyte.infrastructure.container;

import exambyte.application.mapper.export.mapper.AnswerMapper;
import exambyte.domain.model.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import exambyte.application.mapper.export.mapper.AnswerMapperImpl;
import exambyte.infrastructure.repository.AnswerDAO;
import exambyte.infrastructure.repository.AnswerRepositoryImpl;
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
class AnswerDBTest {

    @Autowired
    private AnswerDAO dao;

    private AnswerRepository repository;

    private static final UUID ANSWER_ID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    @BeforeEach
    void setUp() {
        AnswerMapper answerMapper = new AnswerMapperImpl();
        repository = new AnswerRepositoryImpl(dao, answerMapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Answer> loaded = repository.findById(ANSWER_ID);

        // Assert
        assertThat(loaded).isPresent();
    }
}
