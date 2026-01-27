package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.ExamMapper;
import exambyte.domain.repository.ExamRepository;
import exambyte.infrastructure.persistence.entities.ExamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamRepositoryTest {

    private final ExamDAO examDAO = mock(ExamDAO.class);
    private final ExamMapper examMapper = mock(ExamMapper.class);

    private ExamRepository repository;

    private static final LocalDateTime TIMESTAMP =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new ExamRepositoryImpl(examDAO, examMapper);
    }

    @Test
    void findByStartTime_exists() {
        // Arrange
        ExamEntity examEntity = new ExamEntity.ExamEntityBuilder()
                .professorFachId(UUID.randomUUID())
                .title("Exam")
                .startZeitpunkt(TIMESTAMP)
                .endZeitpunkt(TIMESTAMP.plusHours(1))
                .resultZeitpunkt(TIMESTAMP.plusHours(2))
                .build();

        when(examDAO.findByStartZeitpunkt(TIMESTAMP)).thenReturn(Optional.of(examEntity));

        // Act
        Optional<UUID> result = repository.findByStartTime(TIMESTAMP);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(examEntity.getFachId());
    }

    @Test
    void findByStartTime_notFound() {
        // Arrange
        when(examDAO.findByStartZeitpunkt(TIMESTAMP)).thenReturn(Optional.empty());

        // Act
        Optional<UUID> result = repository.findByStartTime(TIMESTAMP);

        // Assert
        assertThat(result).isNotPresent();
    }
}
