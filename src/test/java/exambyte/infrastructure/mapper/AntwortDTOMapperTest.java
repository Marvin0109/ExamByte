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
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(id)
                .antwortText("Antwort")
                .frageId(frageId)
                .studentId(studentId)
                .antwortZeitpunkt(antwortTime)
                .build();

        // Act
        AntwortDTO dto = mapper.toDTO(antwort);

        // Assert
        assertEquals(id, dto.id());
        assertEquals(studentId, dto.studentId());
        assertEquals(frageId, dto.frageId());
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
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        UUID id2 = UUID.randomUUID();
        UUID studentId2 = UUID.randomUUID();
        UUID frageId2 = UUID.randomUUID();
        LocalDateTime antwortTime2 = LocalDateTime.of(2020, 1, 1, 1, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(id)
                .antwortText("Antwort")
                .frageId(frageId)
                .studentId(studentId)
                .antwortZeitpunkt(antwortTime)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .id(id2)
                .antwortText("Antwort2")
                .frageId(frageId2)
                .studentId(studentId2)
                .antwortZeitpunkt(antwortTime2)
                .build();

        List<Antwort> antworten = Arrays.asList(antwort, antwort2);

        // Act
        List<AntwortDTO> dto = mapper.toAntwortDTOList(antworten);

        // Assert
        assertEquals(2, dto.size());
        assertEquals(id, dto.getFirst().id());
        assertEquals(studentId, dto.getFirst().studentId());
        assertEquals(frageId, dto.getFirst().frageId());
        assertEquals(antwortTime, dto.getFirst().antwortZeitpunkt());

        assertEquals(id2, dto.getLast().id());
        assertEquals(studentId2, dto.getLast().studentId());
        assertEquals(frageId2, dto.getLast().frageId());
        assertEquals(antwortTime2, dto.getLast().antwortZeitpunkt());
    }

    @Test
    @DisplayName("toDomain Test")
    void test_04() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        LocalDateTime antwortTime = LocalDateTime.of(2020, 1, 1, 0, 0);

        AntwortDTO dto = new AntwortDTO(
                id,
                "Antwort",
                frageId,
                studentId,
                antwortTime);

        // Act
        Antwort antwort = mapper.toDomain(dto);

        // Assert
        assertEquals(id, antwort.getId());
        assertEquals(studentId, antwort.getStudentUUID());
        assertEquals(frageId, antwort.getFrageId());
        assertEquals(antwortTime, antwort.getAntwortZeitpunkt());
        assertEquals("Antwort", antwort.getAntwortText());
    }
}
