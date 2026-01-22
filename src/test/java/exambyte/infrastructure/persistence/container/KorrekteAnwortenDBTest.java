package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.KorrekteAntwortenMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.repository.KorrekteAntwortenRepository;
import exambyte.infrastructure.persistence.mapper.KorrekteAntwortenMapperImpl;
import exambyte.infrastructure.persistence.repository.KorrekteAntwortenDAO;
import exambyte.infrastructure.persistence.repository.KorrekteAntwortenRepositoryImpl;
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
class KorrekteAnwortenDBTest {

    @Autowired
    private KorrekteAntwortenDAO korrekteAntwortenDAO;

    private KorrekteAntwortenRepository repository;

    private static final UUID KORREKTE_ANWORTEN_UUID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    @BeforeEach
    void setUp() {
        KorrekteAntwortenMapper mapper = new KorrekteAntwortenMapperImpl();
        repository = new KorrekteAntwortenRepositoryImpl(korrekteAntwortenDAO, mapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<KorrekteAntworten> geladen = repository.findByFachId(KORREKTE_ANWORTEN_UUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
