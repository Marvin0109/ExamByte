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

    private static final UUID ANTWORT__ID = UUID.randomUUID();
    private static final UUID FRAGE__ID = UUID.randomUUID();
    private static final UUID STUDENT__ID = UUID.randomUUID();
    private static final LocalDateTime TIMESTAMP =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new AntwortRepositoryImpl(antwortDAO, antwortMapper);
    }

    @Test
    void findByFrageId_exists() {
        // Arrange
        AntwortEntity entity = new AntwortEntity.AntwortEntityBuilder()
                .antwortText("Antwort")
                .frageId(FRAGE__ID)
                .studentId(STUDENT__ID)
                .antwortZeitpunkt(TIMESTAMP)
                .build();
        Antwort domain = new Antwort.AntwortBuilder()
                .antwortText("Antwort")
                .frageId(FRAGE__ID)
                .studentId(STUDENT__ID)
                .antwortZeitpunkt(TIMESTAMP)
                .build();

        when(antwortDAO.findByFrageId(ANTWORT__ID)).thenReturn(Optional.of(entity));
        when(antwortMapper.toDomain(entity)).thenReturn(domain);

        // Act
        Antwort result = repository.findByFrageId(ANTWORT__ID);

        // Assert
        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    void findByFrageId_notExists_returnsNull() {
        // Arrange
        when(antwortDAO.findByFrageId(ANTWORT__ID)).thenReturn(Optional.empty());

        // Act
        Antwort result = repository.findByFrageId(ANTWORT__ID);

        // Assert
        assertNull(result);
    }
}
