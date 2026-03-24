package exambyte.infrastructure.container;

import exambyte.domain.model.user.Student;
import exambyte.infrastructure.mapper.StudentMapper;
import exambyte.infrastructure.mapper.StudentMapperImpl;
import exambyte.infrastructure.repository.StudentDAO;
import exambyte.infrastructure.repository.StudentRepositoryImpl;
import exambyte.domain.repository.StudentRepository;
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
@Sql("/data-test.sql")
class StudentDBTest {

    @Autowired
    private StudentDAO dao;

    private StudentRepository repository;

    private static final UUID STUDENT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @BeforeEach
    void setUp() {
        StudentMapper studentMapper = new StudentMapperImpl();
        repository = new StudentRepositoryImpl(dao, studentMapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Student> loaded = repository.findById(STUDENT_ID);

        // Assert
        assertThat(loaded).isPresent();
    }
}
