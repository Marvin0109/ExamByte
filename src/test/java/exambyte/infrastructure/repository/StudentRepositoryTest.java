package exambyte.infrastructure.repository;

import exambyte.application.mapper.export.mapper.StudentMapper;
import exambyte.domain.repository.StudentRepository;
import exambyte.infrastructure.entity.StudentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentRepositoryTest {

    private final StudentDAO dao = mock(StudentDAO.class);
    private final StudentMapper mapper = mock(StudentMapper.class);

    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepositoryImpl(dao, mapper);
    }

    @Test
    void findIdByName_exists() {
        // Arrange
        String name = "Name";
        StudentEntity student = new StudentEntity.StudentEntityBuilder()
                .id(UUID.randomUUID())
                .name(name)
                .build();

        when(dao.findIdByName(name)).thenReturn(Optional.of(student));

        // Act
        Optional<UUID> result = repository.findIdByName(name);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualByComparingTo(student.getId());
    }

    @Test
    void findIdByName_notFound() {
        // Arrange
        when(dao.findIdByName("Name")).thenReturn(Optional.empty());

        // Act
        Optional<UUID> result = repository.findIdByName("Name");

        // Assert
        assertThat(result).isEmpty();
    }
}
