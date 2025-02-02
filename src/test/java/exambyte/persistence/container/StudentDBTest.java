package exambyte.persistence.container;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.repository.SpringDataStudentRepository;
import exambyte.persistence.repository.StudentRepositoryImpl;
import exambyte.domain.repository.StudentRepository;
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
public class StudentDBTest {

    @Autowired
    private SpringDataStudentRepository studentRepository;
    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepositoryImpl(studentRepository);
    }

    @Test
    @DisplayName("Ein Student kann gespeichert und wieder geladen werden")
    void test_01() {
        // Arrange
        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(null)
                .name("Max Mustermann")
                .build();

        // Act
        repository.save(student);
        Optional<Student> geladen = repository.findByFachId(student.uuid());

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getName()).isEqualTo("Max Mustermann");
        assertThat(geladen.get().uuid()).isEqualTo(student.uuid());
    }
}
