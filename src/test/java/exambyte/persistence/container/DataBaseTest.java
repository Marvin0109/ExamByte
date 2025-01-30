package exambyte.persistence.container;

import exambyte.ExamByteApplication;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.repository.SpringDataStudentRepository;
import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.persistence.mapper.JDBC.StudentMapperJDBC;
import exambyte.persistence.repository.StudentRepositoryImpl;
import exambyte.service.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ExamByteApplication.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class DataBaseTest {

    @Autowired
    private SpringDataStudentRepository studentRepository;
    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepositoryImpl(studentRepository);
    }

    @Test
    @DisplayName("Eine Person kann gespeichert und wieder geladen werden")
    void test_01() {
        // Arrange
        Student student = Student.of(null, null, "Max Mustermann");
        StudentMapperJDBC studentMapper = new StudentMapperJDBC();
        StudentEntityJDBC studentEntityJDBC = studentMapper.toEntity(student);
        repository.save(studentEntityJDBC);
        Optional<StudentEntityJDBC> geladen = repository.findById(studentEntityJDBC.getId());
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getName()).isEqualTo("Max Mustermann");
        assertThat(geladen.get().getId()).isEqualTo(1L);
    }
}
