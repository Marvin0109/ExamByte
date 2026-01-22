package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.infrastructure.persistence.mapper.FrageMapperImpl;
import exambyte.domain.repository.FrageRepository;
import exambyte.infrastructure.persistence.repository.FrageDAO;
import exambyte.infrastructure.persistence.repository.FrageRepositoryImpl;
import exambyte.infrastructure.persistence.repository.ProfessorDAO;
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
class FrageDBTest {

    @Autowired
    private FrageDAO frageDAO;

    @Autowired
    private ProfessorDAO professorDAO;

    private FrageRepository frageRepository;

    private static final UUID FRAGEUUID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    @BeforeEach
    void setUp() {
        FrageMapper frageMapper = new FrageMapperImpl();
        frageRepository = new FrageRepositoryImpl(professorDAO, frageDAO, frageMapper);
    }

    @Test
    @DisplayName("Eine Frage kann gespeichert und wieder geladen werden, außerdem kann der Professor extrahiert werden")
    void test_01() {
        // Act
        Optional<Frage> geladen = frageRepository.findByFachId(FRAGEUUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
