package exambyte.infrastructure.mapper;

import exambyte.application.dto.StudentDTO;
import exambyte.domain.mapper.StudentDTOMapper;
import exambyte.domain.model.aggregate.user.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StudentDTOMapperTest {

    private final StudentDTOMapper mapper = new StudentDTOMapperImpl();

    @Test
    @DisplayName("Test StudentDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID id = UUID.randomUUID();

        Student student = new Student.StudentBuilder()
                .id(id)
                .name("Student Name")
                .build();

        // Act
        StudentDTO studentDTO = mapper.toDTO(student);

        // Assert
        assertEquals(id, studentDTO.id());
        assertEquals("Student Name", studentDTO.name());
    }

    @Test
    @DisplayName("test_null_student_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toStudentDTOList Test")
    void test_03() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Student student1 = new Student.StudentBuilder()
                .id(id)
                .name("Student 1")
                .build();

        Student student2 = new Student.StudentBuilder()
                .id(id2)
                .name("Student 2")
                .build();

        List<Student> students = Arrays.asList(student1, student2);

        // Act
        List<StudentDTO> studentDTOList = mapper.toStudentDTOList(students);

        // Assert
        assertEquals(2, studentDTOList.size());
        assertThat(studentDTOList.getFirst().name()).isEqualTo("Student 1");
        assertThat(studentDTOList.getFirst().id()).isEqualTo(id);
        assertThat(studentDTOList.getLast().name()).isEqualTo("Student 2");
        assertThat(studentDTOList.getLast().id()).isEqualTo(id2);
    }
}
