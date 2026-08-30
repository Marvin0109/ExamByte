package exambyte.infrastructure.container;

import exambyte.domain.model.user.Professor;
import exambyte.infrastructure.mapper.ProfessorMapper;
import exambyte.infrastructure.repository.ProfessorRepositoryImpl;
import exambyte.infrastructure.repository.ProfessorDAO;
import exambyte.domain.repository.ProfessorRepository;
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
class ProfessorDBTest {

    @Autowired
    private ProfessorDAO dao;

    private ProfessorRepository repository;

    private static final UUID PROFESSOR_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @BeforeEach
    void setUp() {
        ProfessorMapper professorMapper = new ProfessorMapper();
        repository = new ProfessorRepositoryImpl(dao, professorMapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Professor> loaded = repository.findById(PROFESSOR_ID);

        // Assert
        assertThat(loaded).isPresent();
    }

    @Test
    void find_id_by_name_success() {
        // Act
        Optional<Professor> id = repository.findByName("ProfTestName");

        // Assert
        assertThat(id).isPresent();
    }
}
