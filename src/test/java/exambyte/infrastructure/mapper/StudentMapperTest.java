package exambyte.infrastructure.mapper;

import exambyte.domain.model.user.Student;
import exambyte.infrastructure.entity.StudentEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTest {

    private final StudentMapper mapper = new StudentMapper();

    @Test
    void toEntity() {
        // Arrange
        Student student = new Student.StudentBuilder()
                .name("Jim Bim")
                .build();

        // Act
        StudentEntity entity = mapper.toEntity(student);
        String entityName = entity.getName();

        // Assert
        assertThat(entityName).isEqualTo("Jim Bim");
    }

    @Test
    void toDomain() {
        // Arrange
        StudentEntity entity = new StudentEntity.StudentEntityBuilder()
                .name("Test student")
                .build();

        // Act
        Student student = mapper.toDomain(entity);

        // Assert
        assertThat(student.getName()).isEqualTo("Test student");
    }
}
