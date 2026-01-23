package exambyte.domain.aggregate.exam;

import exambyte.domain.model.aggregate.exam.Antwort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AntwortTest {

    @Test
    @DisplayName("AntwortBuilder Test")
    void test_01() {
        UUID fachId = UUID.randomUUID();
        String antwortText = "Test answer";
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(fachId)
                .antwortText(antwortText)
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .build();

        assertEquals(fachId, antwort.getFachId());
        assertEquals(antwortText, antwort.getAntwortText());
        assertEquals(frageFachId, antwort.getFrageFachId());
        assertEquals(studentFachId, antwort.getStudentUUID());
        assertEquals(antwortZeitpunkt, antwort.getAntwortZeitpunkt());

    }

    @Test
    @DisplayName("AntwortBuilder Test mit null Werte")
    void test_02() {
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Antwort antwort = new Antwort.AntwortBuilder()
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .build();

        assertNull(antwort.getAntwortText());
        assertEquals(frageFachId, antwort.getFrageFachId());
        assertEquals(studentFachId, antwort.getStudentUUID());
        assertEquals(antwortZeitpunkt, antwort.getAntwortZeitpunkt());
    }

}
