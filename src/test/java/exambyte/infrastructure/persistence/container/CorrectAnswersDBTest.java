package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.CorrectAnswersMapper;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.domain.repository.CorrectAnswersRepository;
import exambyte.infrastructure.persistence.mapper.CorrectAnswersMapperImpl;
import exambyte.infrastructure.persistence.repository.CorrectAnswersDAO;
import exambyte.infrastructure.persistence.repository.CorrectAnswersRepositoryImpl;
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
class CorrectAnswersDBTest {

    @Autowired
    private CorrectAnswersDAO correctAnswersDAO;

    private CorrectAnswersRepository repository;

    private static final UUID CORRECT_ANSWERS_UUID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    @BeforeEach
    void setUp() {
        CorrectAnswersMapper mapper = new CorrectAnswersMapperImpl();
        repository = new CorrectAnswersRepositoryImpl(correctAnswersDAO, mapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<CorrectAnswers> geladen = repository.findById(CORRECT_ANSWERS_UUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
