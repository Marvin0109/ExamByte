package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTest {

    private final StudentMapper studentMapper = new StudentMapperImpl();

    @Test
    void toEntity() {
        // Arrange
        Student student = new Student.StudentBuilder()
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
    void toDomain() {
        // Arrange
        StudentEntity studentEntity = new StudentEntity.StudentEntityBuilder()
                .name("Test student")
                .build();

        // Act
        Student student = studentMapper.toDomain(studentEntity);

        // Assert
        assertThat(student.getName()).isEqualTo("Test student");
    }
}
