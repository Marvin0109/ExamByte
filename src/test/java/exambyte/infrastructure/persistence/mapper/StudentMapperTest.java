package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.infrastructure.persistence.entities.StudentEntity;
import exambyte.infrastructure.persistence.mapper.StudentMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentMapperTest {

    @Test
    @DisplayName("StudentMapper test 'toEntity'")
    public void test_01() {
        // Arrange
        StudentMapper studentMapper = new StudentMapperImpl();

        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(null)
                .name("Jim Bim")
                .build();

        // Act
        StudentEntity studentEntity = studentMapper.toEntity(student);
        UUID entityFachId = studentEntity.getFachId();
        String entityName = studentEntity.getName();

        // Assert
        assertThat(studentEntity).isNotNull();
        assertThat(entityFachId).isEqualTo(student.uuid());
        assertThat(entityName).isEqualTo("Jim Bim");
    }

    @Test
    @DisplayName("StudentMapper test 'toDomain'")
    public void test_02() {
        // Arrange
        StudentMapper studentMapper = new StudentMapperImpl();

        StudentEntity studentEntity = new StudentEntity.StudentEntityBuilder()
                .id(null)
                .fachId(null)
                .name("John Tectone")
                .build();

        // Act
        Student student = studentMapper.toDomain(studentEntity);
        UUID studentFachId = student.uuid();
        String studentName = student.getName();

        // Assert
        assertThat(student).isNotNull();
        assertThat(studentFachId).isEqualTo(studentEntity.getFachId());
        assertThat(studentName).isEqualTo("John Tectone");
    }
}
