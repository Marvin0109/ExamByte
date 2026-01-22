package exambyte.infrastructure.persistence.container;

import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.infrastructure.persistence.mapper.AntwortMapperImpl;
import exambyte.infrastructure.persistence.repository.AntwortDAO;
import exambyte.infrastructure.persistence.repository.AntwortRepositoryImpl;
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
class AntwortDBTest {

    @Autowired
    private AntwortDAO antwortDAO;

    private AntwortRepository antwortRepository;

    private static final UUID ANTWORTUUID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    @BeforeEach
    void setUp() {
        AntwortMapper antwortMapper = new AntwortMapperImpl();
        antwortRepository = new AntwortRepositoryImpl(antwortDAO, antwortMapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<Antwort> geladen = antwortRepository.findByFachId(ANTWORTUUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
