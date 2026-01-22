package exambyte.domain.aggregate.exam;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class KorrekteAntwortTest {

    @Test
    @DisplayName("KorrekteAntwortenBuilder Test")
    public void test_01() {
        Long id = 1L;
        UUID fachId = UUID.randomUUID();
        UUID frageFachID = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1\nLösung 2";
        String antwort_optionen = "Lösung 1\nLösung 2\nLösung 3";

        KorrekteAntworten domain = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(id)
                .fachId(fachId)
                .frageFachId(frageFachID)
                .loesungen(korrekteAntworten)
                .antwortOptionen(antwort_optionen)
                .build();

        assertEquals(id, domain.getId());
        assertEquals(fachId, domain.getFachId());
        assertEquals(frageFachID, domain.getFrageFachId());
        assertThat(domain.getLoesungen()).contains(korrekteAntworten);
        assertThat(domain.getAntwortOptionen()).contains(antwort_optionen);
    }

    @Test
    @DisplayName("KorrekteAntwortenBuilder mit null Werte (FachID wird bei null generiert)")
    public void test_02() {
        KorrekteAntworten domain = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .build();

        assertNull(domain.getId());
        assertNotNull(domain.getFachId());
        assertNull(domain.getFrageFachId());
        assertNull(domain.getLoesungen());
        assertNull(domain.getAntwortOptionen());
    }
}
