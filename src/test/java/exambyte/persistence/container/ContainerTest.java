package exambyte.persistence.container;

import exambyte.application.ExamByteApplication;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.mapper.ProfessorMapper;
import exambyte.persistence.repository.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ProfessorMapper.class, ProfessorRepository.class, TestcontainerConfiguration.class})
public class ContainerTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    @Test
    @DisplayName("Ein Aggregat kann gespeichert")
    void test_01() throws Exception {
        // Arrange
        Professor professor = Professor.of(1L, "Professor");
        ProfessorEntity professorEntity = professorMapper.toEntity(professor);

        // Act
        professorRepository.save(professorEntity);

        // Assert
        assertThat(professorRepository.count()).isEqualTo(1);
    }

    // ...
}
