package exambyte.infrastructure.mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.model.aggregate.exam.Antwort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AntwortDTOMapperTest {

    private final AntwortDTOMapper mapper = new AntwortDTOMapperImpl();

    @Test
    @DisplayName("Test AntwortDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.now();
        LocalDateTime lastChangesTime = LocalDateTime.now();

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(fachId)
                .antwortText("Antwort")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChangesTime)
                .build();

        // Act
        AntwortDTO dto = mapper.toDTO(antwort);

        // Assert
        assertNull(dto.getId());
        assertEquals(fachId, dto.getFachId());
        assertEquals(studentFachId, dto.getStudentFachId());
        assertEquals(frageFachId, dto.getFrageFachId());
        assertEquals("Antwort", dto.getAntwortText());
        assertEquals(antwortTime, dto.getAntwortZeitpunkt());
        assertEquals(lastChangesTime, dto.getLastChangesZeitpunkt());
    }

    @Test
    @DisplayName("test_null_antwort_throws_exception")
    void test_02() {
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("toAntwortDTOList Test")
    void test_03() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.now();
        LocalDateTime lastChangesTime = LocalDateTime.now();

        UUID fachId2 = UUID.randomUUID();
        UUID studentFachId2 = UUID.randomUUID();
        UUID frageFachId2 = UUID.randomUUID();
        LocalDateTime antwortTime2 = LocalDateTime.now();
        LocalDateTime lastChangesTime2 = LocalDateTime.now();

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(fachId)
                .antwortText("Antwort")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChangesTime)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(fachId2)
                .antwortText("Antwort2")
                .frageFachId(frageFachId2)
                .studentFachId(studentFachId2)
                .antwortZeitpunkt(antwortTime2)
                .lastChangesZeitpunkt(lastChangesTime2)
                .build();

        List<Antwort> antworten = Arrays.asList(antwort, antwort2);

        // Act
        List<AntwortDTO> dto = mapper.toAntwortDTOList(antworten);

        // Assert
        assertEquals(2, dto.size());
        assertEquals(fachId, dto.getFirst().getFachId());
        assertEquals(studentFachId, dto.getFirst().getStudentFachId());
        assertEquals(frageFachId, dto.getFirst().getFrageFachId());
        assertEquals(antwortTime, dto.getFirst().getAntwortZeitpunkt());
        assertEquals(lastChangesTime, dto.getFirst().getLastChangesZeitpunkt());

        assertEquals(fachId2, dto.getLast().getFachId());
        assertEquals(studentFachId2, dto.getLast().getStudentFachId());
        assertEquals(frageFachId2, dto.getLast().getFrageFachId());
        assertEquals(antwortTime2, dto.getLast().getAntwortZeitpunkt());
        assertEquals(lastChangesTime2, dto.getLast().getLastChangesZeitpunkt());
    }

    @Test
    @DisplayName("toDomain Test")
    void test_04() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.now();
        LocalDateTime lastChangesTime = LocalDateTime.now();

        AntwortDTO dto = new AntwortDTO.AntwortDTOBuilder()
                .fachId(fachId)
                .antwortText("Antwort")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortTime)
                .lastChangesZeitpunkt(lastChangesTime)
                .build();

        // Act
        Antwort antwort = mapper.toDomain(dto);

        // Assert
        assertEquals(fachId, antwort.getFachId());
        assertEquals(studentFachId, antwort.getStudentUUID());
        assertEquals(frageFachId, antwort.getFrageFachId());
        assertEquals(antwortTime, antwort.getAntwortZeitpunkt());
        assertEquals(lastChangesTime, antwort.getLastChangesZeitpunkt());
        assertEquals("Antwort", antwort.getAntwortText());
    }
}
