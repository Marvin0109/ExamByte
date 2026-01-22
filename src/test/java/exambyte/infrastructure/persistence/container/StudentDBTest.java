package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.infrastructure.persistence.mapper.StudentMapperImpl;
import exambyte.infrastructure.persistence.repository.StudentDAO;
import exambyte.infrastructure.persistence.repository.StudentRepositoryImpl;
import exambyte.domain.repository.StudentRepository;
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
@Sql("/data-test.sql")
class StudentDBTest {

    @Autowired
    private StudentDAO studentDAO;

    private StudentRepository repository;

    private static final UUID STUDENTUUID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @BeforeEach
    void setUp() {
        StudentMapper studentMapper = new StudentMapperImpl();
        repository = new StudentRepositoryImpl(studentDAO, studentMapper);
    }

    @Test
    @DisplayName("Ein Student kann geladen werden")
    void test_01() {
        // Act
        Optional<Student> geladen = repository.findByFachId(STUDENTUUID);

        // Assert
        assertThat(geladen).isPresent();
    }
}
