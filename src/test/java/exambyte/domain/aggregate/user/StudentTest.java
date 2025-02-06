package exambyte.domain.aggregate.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    // Create Student with all fields populated using builder pattern
    @Test
    @DisplayName("create_student_with_all_fields")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        String name = "John Doe";

        Student student = new Student.StudentBuilder()
                .id(id)
                .fachId(fachId)
                .name(name)
                .build();

        assertEquals(id, student.getId());
        assertEquals(fachId, student.uuid());
        assertEquals(name, student.getName());
    }

    // Create Student with null id
    @Test
    @DisplayName("create_student_with_null_id")
    public void test_02() {
        UUID fachId = UUID.randomUUID();
        String name = "John Doe";

        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(fachId)
                .name(name)
                .build();

        assertNull(student.getId());
        assertEquals(fachId, student.uuid());
        assertEquals(name, student.getName());
    }
}
