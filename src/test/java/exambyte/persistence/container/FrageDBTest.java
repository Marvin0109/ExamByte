package exambyte.persistence.container;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.mapper.JDBC.FrageMapperJDBC;
import exambyte.persistence.mapper.JDBC.ProfessorMapperJDBC;
import exambyte.persistence.repository.*;
import exambyte.service.FrageRepository;
import exambyte.service.ProfessorRepository;
import exambyte.service.StudentRepository;
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
public class FrageDBTest {

    @Autowired
    private SpringDataFrageRepository frageRepository;

    @Autowired
    private SpringDataProfessorRepository professorRepository;

    private FrageRepository repository;
    private ProfessorRepository repository2;

    @BeforeEach
    void setUp() {
        repository = new FrageRepositoryImpl(professorRepository, frageRepository);
        repository2 = new ProfessorRepositoryImpl(professorRepository);
    }

    @Test
    @DisplayName("Eine Frage kann gespeichert und wieder geladen werden, außerdem kann der Professor extrahiert werden")
    void test_01() {
        // Arrange
        Professor professor = Professor.of(null, null, "Dr. Lowkey");
        ProfessorMapperJDBC professorMapper = new ProfessorMapperJDBC();
        ProfessorEntityJDBC professorEntityJDBC = professorMapper.toEntity(professor);

        repository2.save(professorEntityJDBC);

        Frage frage = Frage.of(null, null, "Was ist Java?", professorEntityJDBC.getFachId());
        FrageMapperJDBC frageMapperJDBC = new FrageMapperJDBC(professorMapper, (FrageRepositoryImpl) repository);
        FrageEntityJDBC frageEntity = frageMapperJDBC.toEntity(frage);

        // Act
        repository.save(frageEntity);

        Optional<FrageEntityJDBC> geladen = frageRepository.findByFachId(frageEntity.getFachId());
        ProfessorEntityJDBC extraction = ((FrageRepositoryImpl) repository).findByProfFachId(professorEntityJDBC.getFachId());

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getFrageText()).isEqualTo("Was ist Java?");
        assertThat(geladen.get().getFachId()).isEqualTo(frageEntity.getFachId());

        assertThat(extraction.getFachId()).isEqualTo(professorEntityJDBC.getFachId());
        assertThat(extraction.getName()).isEqualTo("Dr. Lowkey");
    }
}
