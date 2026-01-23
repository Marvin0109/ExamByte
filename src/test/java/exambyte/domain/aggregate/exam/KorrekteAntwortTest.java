package exambyte.domain.aggregate.exam;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KorrekteAntwortTest {

    @Test
    @DisplayName("KorrekteAntwortenBuilder Test")
    void test_01() {
        UUID fachId = UUID.randomUUID();
        UUID frageFachID = UUID.randomUUID();
        String korrekteAntworten = "Lösung 1\nLösung 2";
        String antwortOptionen = "Lösung 1\nLösung 2\nLösung 3";

        KorrekteAntworten domain = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(fachId)
                .frageFachId(frageFachID)
                .loesungen(korrekteAntworten)
                .antwortOptionen(antwortOptionen)
                .build();

        assertEquals(fachId, domain.getFachId());
        assertEquals(frageFachID, domain.getFrageFachId());
        assertThat(domain.getLoesungen()).contains(korrekteAntworten);
        assertThat(domain.getAntwortOptionen()).contains(antwortOptionen);
    }
}
