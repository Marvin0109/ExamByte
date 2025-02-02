package exambyte.persistence.container;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.repository.ProfessorRepositoryImpl;
import exambyte.persistence.repository.SpringDataProfessorRepository;
import exambyte.domain.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class ProfessorDBTest {

    @Autowired
    private SpringDataProfessorRepository professorRepository;
    private ProfessorRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProfessorRepositoryImpl(professorRepository);
    }

    @Test
    @DisplayName("Ein Professor kann gespeichert und wieder geladen werden")
    void test_01() {
        // Arrange
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name("Dr. KekW")
                .build();

        // Act
        repository.save(professor);
        Optional<Professor> geladen = repository.findByFachId(professor.uuid());

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getName()).isEqualTo("Dr. KekW");
        assertThat(geladen.get().uuid()).isEqualTo(professor.uuid());
    }
}
