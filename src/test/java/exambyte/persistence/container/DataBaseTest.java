package exambyte.persistence.container;

import exambyte.application.ExamByteApplication;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.JDBC.repository.StudentRepository;
import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.persistence.mapper.JDBC.StudentMapperJDBC;
import exambyte.persistence.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ExamByteApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataBaseTest {

    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("Eine Person kann gespeichert und wieder geladen werden")
    void test_01() {
        // Arrange
        Student student = Student.of(null, "Max Mustermann");
        StudentEntityJDBC studentEntityJDBC = new StudentMapperJDBC().toEntity(student);

        // Act
        StudentEntityJDBC savedStudent = studentService.saveStudent(studentEntityJDBC);
        Optional<StudentEntityJDBC> found = studentService.findStudentById(savedStudent.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Max Mustermann");
    }
}
