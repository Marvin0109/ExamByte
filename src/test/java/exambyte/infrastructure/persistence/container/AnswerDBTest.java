package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.AnswerMapper;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import exambyte.infrastructure.persistence.mapper.AnswerMapperImpl;
import exambyte.infrastructure.persistence.repository.AnswerDAO;
import exambyte.infrastructure.persistence.repository.AnswerRepositoryImpl;
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
class AnswerDBTest {

    @Autowired
    private AnswerDAO answerDAO;

    private AnswerRepository answerRepository;

    private static final UUID ANSWER_UUID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    @BeforeEach
    void setUp() {
        AnswerMapper answerMapper = new AnswerMapperImpl();
        answerRepository = new AnswerRepositoryImpl(answerDAO, answerMapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<Answer> geladen = answerRepository.findById(ANSWER_UUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
