package exambyte.domain.aggregate.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AntwortTest {
    @Test
    @DisplayName("test_builder_creates_antwort_with_all_fields")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        String antwortText = "Test answer";
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.now();
        LocalDateTime lastChangesZeitpunkt = LocalDateTime.now();

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(id)
                .fachId(fachId)
                .antwortText(antwortText)
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(lastChangesZeitpunkt)
                .build();

        assertEquals(id, antwort.getId());
        assertEquals(fachId, antwort.getFachId());
        assertEquals(antwortText, antwort.getAntwortText());
        assertEquals(frageFachId, antwort.getFrageFachId());
        assertEquals(studentFachId, antwort.getStudentUUID());
        assertEquals(antwortZeitpunkt, antwort.getAntwortZeitpunkt());
        assertEquals(lastChangesZeitpunkt, antwort.getLastChangesZeitpunkt());
    }
    @Test
    @DisplayName("test_builder_creates_antwort_with_null_optional_fields")
    public void test_02() {
        Long id = 1L;
        UUID frageFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.now();

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(id)
                .fachId(null)
                .antwortText(null)
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(null)
                .build();

        assertEquals(id, antwort.getId());
        assertNotNull(antwort.getFachId());
        assertNull(antwort.getAntwortText());
        assertEquals(frageFachId, antwort.getFrageFachId());
        assertEquals(studentFachId, antwort.getStudentUUID());
        assertEquals(antwortZeitpunkt, antwort.getAntwortZeitpunkt());
        assertNull(antwort.getLastChangesZeitpunkt());
    }

}
