package exambyte.infrastructure.repository;

import exambyte.application.mapper.export.mapper.ExamMapper;
import exambyte.domain.repository.ExamRepository;
import exambyte.infrastructure.entity.ExamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamRepositoryTest {

    private final ExamDAO dao = mock(ExamDAO.class);
    private final ExamMapper mapper = mock(ExamMapper.class);

    private ExamRepository repository;

    private static final LocalDateTime START =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new ExamRepositoryImpl(dao, mapper);
    }

    @Test
    void findByStartTime_exists() {
        // Arrange
        ExamEntity exam = new ExamEntity.ExamEntityBuilder()
                .id(UUID.randomUUID())
                .professorId(UUID.randomUUID())
                .title("Exam")
                .start(START)
                .end(START.plusHours(1))
                .result(START.plusHours(2))
                .build();

        when(dao.findByStart(START)).thenReturn(Optional.of(exam));

        // Act
        Optional<UUID> result = repository.findByStartTime(START);

        // Assert
        assertThat(result).isPresent();
        assertEquals(result.get(), exam.getId());
    }

    @Test
    void findByStartTime_notFound() {
        // Arrange
        when(dao.findByStart(START)).thenReturn(Optional.empty());

        // Act
        Optional<UUID> result = repository.findByStartTime(START);

        // Assert
        assertThat(result).isNotPresent();
    }
}
