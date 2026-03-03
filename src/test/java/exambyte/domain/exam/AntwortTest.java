package exambyte.domain.exam;

import exambyte.domain.model.aggregate.exam.Antwort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AntwortTest {

    @Test
    @DisplayName("AntwortBuilder Test")
    void test_01() {
        UUID id = UUID.randomUUID();
        String antwortText = "Test answer";
        UUID frageId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(id)
                .antwortText(antwortText)
                .frageId(frageId)
                .studentId(studentId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .build();

        assertEquals(id, antwort.getId());
        assertEquals(antwortText, antwort.getAntwortText());
        assertEquals(frageId, antwort.getFrageId());
        assertEquals(studentId, antwort.getStudentUUID());
        assertEquals(antwortZeitpunkt, antwort.getAntwortZeitpunkt());

    }

    @Test
    @DisplayName("AntwortBuilder Test mit null Werte")
    void test_02() {
        UUID frageId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .frageId(frageId)
                .studentId(studentId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .build();

        assertNull(antwort.getAntwortText());
        assertEquals(frageId, antwort.getFrageId());
        assertEquals(studentId, antwort.getStudentUUID());
        assertEquals(antwortZeitpunkt, antwort.getAntwortZeitpunkt());
    }

}
