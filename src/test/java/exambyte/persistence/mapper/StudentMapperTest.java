package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentMapperTest {

    @Test
    @DisplayName("Umwandlung von Entität zum Objekt (toDomain)")
    void test_01() {
        // Arrange
        StudentMapper studentMapper = new StudentMapper();
        StudentEntity studentEntity = new StudentEntity(1L, "Max Mustermann");

        // Act
        Student student = studentMapper.toDomain(studentEntity);

        // Assert
        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("Max Mustermann", student.getName());
    }

    @Test
    @DisplayName("Umwandlung vom Objekt zur Entität (toEntity)")
    void test_02() {
        // Arrange
        StudentMapper studentMapper = new StudentMapper();
        Student student = Student.of(1L, "Max Mustermann");

        // Act
        StudentEntity studentEntity = studentMapper.toEntity(student);

        // Assert
        assertNotNull(studentEntity);
        assertEquals(1L, studentEntity.getId());
        assertEquals("Max Mustermann", studentEntity.getName());
    }
}
