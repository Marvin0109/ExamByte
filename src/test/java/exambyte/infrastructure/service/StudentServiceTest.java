package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.repository.StudentRepository;
import exambyte.domain.service.StudentService;
import exambyte.infrastructure.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final StudentService studentService = new StudentServiceImpl(studentRepository);

    @Test
    @DisplayName("Ein Student kann nach seiner FachID geladen werden")
    void test_01() {
        // Arrange
        UUID studentFachId = UUID.randomUUID();
        Student student = new Student.StudentBuilder().fachId(studentFachId).build();

        when(studentRepository.findByFachId(studentFachId)).thenReturn(Optional.of(student));

        // Act
        var result = studentService.getStudent(studentFachId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.uuid()).isEqualTo(studentFachId);
        verify(studentRepository).findByFachId(studentFachId);
    }

    @Test
    @DisplayName("Ein Student kann nicht gefunden werden mit einer FachID")
    void test_02() {
        UUID studentFachId = UUID.randomUUID();
        when(studentRepository.findByFachId(studentFachId)).thenReturn(Optional.empty());
        assertThrows(NichtVorhandenException.class, () -> studentService.getStudent(studentFachId));
        verify(studentRepository).findByFachId(studentFachId);
    }

    @Test
    @DisplayName("Ein Student kann erfolgreich gespeichert werden")
    void test_03() {
        // Arrange
        String name = "new_student";

        // Act
        studentService.saveStudent(name);

        // Assert
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Ein Student kann nach seinem Namen geladen werden")
    void test_04() {
        // Arrange
        var student = new Student.StudentBuilder().name("new_student").build();

        when(studentRepository.findByName("new_student")).thenReturn(Optional.of(student));

        // Act
        var result = studentService.getStudentByName("new_student");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get().getName()).isEqualTo("new_student");
        verify(studentRepository).findByName("new_student");
    }

    @Test
    @DisplayName("Ein Student kann nach seiner FachID geladen werden")
    void test_05() {
        // Arrange
        UUID studentFachId = UUID.randomUUID();

        when(studentRepository.findFachIdByName("new_student")).thenReturn(Optional.of(studentFachId));

        // Act
        var result = studentService.getStudentFachId("new_student");

        // Assert
        assertThat(result).isEqualTo(studentFachId);
        verify(studentRepository).findFachIdByName("new_student");
    }
}
