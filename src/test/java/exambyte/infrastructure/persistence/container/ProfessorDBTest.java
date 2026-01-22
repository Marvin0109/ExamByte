package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.infrastructure.persistence.mapper.ProfessorMapperImpl;
import exambyte.infrastructure.persistence.repository.ProfessorRepositoryImpl;
import exambyte.infrastructure.persistence.repository.ProfessorDAO;
import exambyte.domain.repository.ProfessorRepository;
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
class ProfessorDBTest {

    @Autowired
    private ProfessorDAO professorDAO;

    private ProfessorRepository repository;

    private static final UUID PROFUUID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @BeforeEach
    void setUp() {
        ProfessorMapper professorMapper = new ProfessorMapperImpl();
        repository = new ProfessorRepositoryImpl(professorDAO, professorMapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<Professor> geladen = repository.findByFachId(PROFUUID);

        // Assert
        assertThat(geladen).isPresent();
    }

    @Test
    @DisplayName("Die FachId eines Professor kann nach dem Namen gefunden werden")
    void test_02() {
        // Act
        Optional<Professor> fachId = repository.findByName("ProfTestName");

        // Assert
        assertThat(fachId).isPresent();
    }
}
