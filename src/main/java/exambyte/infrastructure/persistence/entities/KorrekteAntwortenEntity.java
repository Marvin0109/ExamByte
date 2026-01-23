package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("correct_answers")
public class KorrekteAntwortenEntity {

    @Id
    private Long id;

    @Column("fach_id")
    private final UUID fachID;

    @Column("frage_id")
    private final UUID frageFachID;

    @Column("richtige_antwort")
    private final String richtigeAntwort;

    @Column("antwort_optionen")
    private final String antwortOptionen;

    private KorrekteAntwortenEntity(UUID fachID, UUID frageFachID, String richtigeAntwort, String antwortOptionen) {
        this.fachID = fachID != null ? fachID : UUID.randomUUID();
        this.frageFachID = frageFachID;
        this.richtigeAntwort = richtigeAntwort;
        this.antwortOptionen = antwortOptionen;
    }

    public UUID getFachID() {
        return fachID;
    }

    public UUID getFrageFachID() {
        return frageFachID;
    }

    public String getRichtigeAntwort() {
        return richtigeAntwort;
    }

    public String getAntwortOptionen() { return antwortOptionen; }

    public static class KorrekteAntwortenEntityBuilder {
        private UUID fachID;
        private UUID frageFachID;
        private String richtigeAntwort;
        private String antwortOptionen;

        public KorrekteAntwortenEntityBuilder fachID(UUID fachID) {
            this.fachID = fachID;
            return this;
        }

        public KorrekteAntwortenEntityBuilder frageFachID(UUID frageFachID) {
            this.frageFachID = frageFachID;
            return this;
        }

        public KorrekteAntwortenEntityBuilder richtigeAntwort(String richtigeAntwort) {
            this.richtigeAntwort = richtigeAntwort;
            return this;
        }

        public KorrekteAntwortenEntityBuilder antwortOptionen(String antwortOptionen) {
            this.antwortOptionen = antwortOptionen;
            return this;
        }

        public KorrekteAntwortenEntity build() {
            return new KorrekteAntwortenEntity(fachID, frageFachID, richtigeAntwort, antwortOptionen);
        }
    }
}
