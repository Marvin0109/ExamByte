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

class AntwortDTOMapperTest {

    private final AntwortDTOMapper mapper = new AntwortDTOMapperImpl();

    @Test
    @DisplayName("Test AntwortDTOMapper 'toDTO'")
    void test_01() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(fachId)
                .antwortText("Antwort")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortTime)
                .build();

        // Act
        AntwortDTO dto = mapper.toDTO(antwort);

        // Assert
        assertEquals(fachId, dto.fachId());
        assertEquals(studentFachId, dto.studentFachId());
        assertEquals(frageFachId, dto.frageFachId());
        assertEquals("Antwort", dto.antwortText());
        assertEquals(antwortTime, dto.antwortZeitpunkt());
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
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        UUID fachId2 = UUID.randomUUID();
        UUID studentFachId2 = UUID.randomUUID();
        UUID frageFachId2 = UUID.randomUUID();
        LocalDateTime antwortTime2 = LocalDateTime.of(2020, 1, 1, 1, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(fachId)
                .antwortText("Antwort")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortTime)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .fachId(fachId2)
                .antwortText("Antwort2")
                .frageFachId(frageFachId2)
                .studentFachId(studentFachId2)
                .antwortZeitpunkt(antwortTime2)
                .build();

        List<Antwort> antworten = Arrays.asList(antwort, antwort2);

        // Act
        List<AntwortDTO> dto = mapper.toAntwortDTOList(antworten);

        // Assert
        assertEquals(2, dto.size());
        assertEquals(fachId, dto.getFirst().fachId());
        assertEquals(studentFachId, dto.getFirst().studentFachId());
        assertEquals(frageFachId, dto.getFirst().frageFachId());
        assertEquals(antwortTime, dto.getFirst().antwortZeitpunkt());

        assertEquals(fachId2, dto.getLast().fachId());
        assertEquals(studentFachId2, dto.getLast().studentFachId());
        assertEquals(frageFachId2, dto.getLast().frageFachId());
        assertEquals(antwortTime2, dto.getLast().antwortZeitpunkt());
    }

    @Test
    @DisplayName("toDomain Test")
    void test_04() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        AntwortDTO dto = new AntwortDTO(
                fachId,
                "Antwort",
                frageFachId,
                studentFachId,
                antwortTime);

        // Act
        Antwort antwort = mapper.toDomain(dto);

        // Assert
        assertEquals(fachId, antwort.getFachId());
        assertEquals(studentFachId, antwort.getStudentUUID());
        assertEquals(frageFachId, antwort.getFrageFachId());
        assertEquals(antwortTime, antwort.getAntwortZeitpunkt());
        assertEquals("Antwort", antwort.getAntwortText());
    }
}
