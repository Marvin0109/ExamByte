package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.StudentMapper;
import exambyte.domain.repository.StudentRepository;
import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentRepositoryTest {

    private final StudentDAO studentDAO = mock(StudentDAO.class);
    private final StudentMapper studentMapper = mock(StudentMapper.class);

    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepositoryImpl(studentDAO, studentMapper);
    }

    @Test
    void findFachIdByName_exists() {
        // Arrange
        String name = "Name";
        StudentEntity student = new StudentEntity.StudentEntityBuilder()
                .name(name)
                .build();

        when(studentDAO.findFachIdByName(name)).thenReturn(Optional.of(student));

        // Act
        Optional<UUID> result = repository.findFachIdByName(name);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualByComparingTo(student.getFachId());
    }

    @Test
    void findFachIdByName_notFound() {
        // Arrange
        when(studentDAO.findFachIdByName("Name")).thenReturn(Optional.empty());

        // Act
        Optional<UUID> result = repository.findFachIdByName("Name");

        // Assert
        assertThat(result).isEmpty();
    }
}
