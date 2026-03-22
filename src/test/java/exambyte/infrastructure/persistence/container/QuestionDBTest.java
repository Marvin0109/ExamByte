package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.entitymapper.QuestionMapper;
import exambyte.infrastructure.persistence.mapper.QuestionMapperImpl;
import exambyte.domain.repository.QuestionRepository;
import exambyte.infrastructure.persistence.repository.QuestionDAO;
import exambyte.infrastructure.persistence.repository.QuestionRepositoryImpl;
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
class QuestionDBTest {

    @Autowired
    private QuestionDAO questionDAO;

    private QuestionRepository questionRepository;

    private static final UUID FRAGEUUID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    @BeforeEach
    void setUp() {
        QuestionMapper questionMapper = new QuestionMapperImpl();
        questionRepository = new QuestionRepositoryImpl(questionDAO, questionMapper);
    }

    @Test
    @DisplayName("Eine Question kann gespeichert und wieder geladen werden, außerdem kann der Professor extrahiert werden")
    void test_01() {
        // Act
        Optional<Question> geladen = questionRepository.findById(FRAGEUUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
