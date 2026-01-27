package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.repository.AntwortRepository;
import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AntwortRepositoryTest {

    private final AntwortDAO antwortDAO = mock(AntwortDAO.class);
    private final AntwortMapper antwortMapper = mock(AntwortMapper.class);

    private AntwortRepository repository;

    private static final UUID ANTWORT_FACH_ID = UUID.randomUUID();
    private static final UUID FRAGE_FACH_ID = UUID.randomUUID();
    private static final UUID STUDENT_FACH_ID = UUID.randomUUID();
    private static final LocalDateTime TIMESTAMP =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new AntwortRepositoryImpl(antwortDAO, antwortMapper);
    }

    @Test
    void findByFrageFachId_exists() {
        // Arrange
        AntwortEntity entity = new AntwortEntity.AntwortEntityBuilder()
                .antwortText("Antwort")
                .frageFachId(FRAGE_FACH_ID)
                .studentFachId(STUDENT_FACH_ID)
                .antwortZeitpunkt(TIMESTAMP)
                .build();
        Antwort domain = new Antwort.AntwortBuilder()
                .antwortText("Antwort")
                .frageFachId(FRAGE_FACH_ID)
                .studentFachId(STUDENT_FACH_ID)
                .antwortZeitpunkt(TIMESTAMP)
                .build();

        when(antwortDAO.findByFrageFachId(ANTWORT_FACH_ID)).thenReturn(Optional.of(entity));
        when(antwortMapper.toDomain(entity)).thenReturn(domain);

        // Act
        Antwort result = repository.findByFrageFachId(ANTWORT_FACH_ID);

        // Assert
        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    void findByFrageFachId_notExists_returnsNull() {
        // Arrange
        when(antwortDAO.findByFrageFachId(ANTWORT_FACH_ID)).thenReturn(Optional.empty());

        // Act
        Antwort result = repository.findByFrageFachId(ANTWORT_FACH_ID);

        // Assert
        assertNull(result);
    }
}
