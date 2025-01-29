package exambyte.persistence.JDBC;

import exambyte.application.ExamByteApplication;
import exambyte.persistence.JDBC.repository.StudentRepository;
import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.persistence.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJdbcTest
@ContextConfiguration(classes = ExamByteApplication.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("Test für Speichern eines Studenten")
    void test_01() {
        // Arrange
        StudentEntityJDBC student = new StudentEntityJDBC(1L, "Max Mustermann");
        when(studentRepository.save(student)).thenReturn(student);

        // Act
        StudentEntityJDBC savedStudent = studentRepository.save(student);

        // Assert
        assertNotNull(savedStudent);
        assertEquals(1L, savedStudent.getId());
        assertEquals("Max Mustermann", savedStudent.getName());

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    @DisplayName("Test für das Finden eines Studenten nach ID")
    void test_02() {
        // Arrange
        StudentEntityJDBC student = new StudentEntityJDBC(1L, "Max Mustermann");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Optional<StudentEntityJDBC> foundStudent = studentService.findStudentById(1L);

        // Assert
        assertTrue(foundStudent.isPresent());
        assertEquals(1L, foundStudent.get().getId());
        assertEquals("Max Mustermann", foundStudent.get().getName());
    }
}
