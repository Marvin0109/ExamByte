package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("correct_answers")
public class KorrekteAntwortenEntity {

    @Id
    private UUID id;

    @Column("frage_id")
    private final UUID frageId;

    @Column("richtige_antwort")
    private final String richtigeAntwort;

    @Column("antwort_optionen")
    private final String antwortOptionen;

    private KorrekteAntwortenEntity(UUID id, UUID frageId, String richtigeAntwort, String antwortOptionen) {
        this.id = id;
        this.frageId = frageId;
        this.richtigeAntwort = richtigeAntwort;
        this.antwortOptionen = antwortOptionen;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFrageId() {
        return frageId;
    }

    public String getRichtigeAntwort() {
        return richtigeAntwort;
    }

    public String getAntwortOptionen() { return antwortOptionen; }

    public static class KorrekteAntwortenEntityBuilder {
        private UUID id;
        private UUID frageId;
        private String richtigeAntwort;
        private String antwortOptionen;

        public KorrekteAntwortenEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public KorrekteAntwortenEntityBuilder frageId(UUID frageId) {
            this.frageId = frageId;
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
            if (frageId == null) {
                throw new IllegalStateException("Frage-ID fehlt");
            }
            checkStringField(richtigeAntwort, "Lösungen fehlen");
            checkStringField(antwortOptionen, "Antwort Optionen fehlen");
            return new KorrekteAntwortenEntity(id, frageId, richtigeAntwort, antwortOptionen);
        }

        private static void checkStringField(String field, String message) {
            if (field == null || field.isBlank()) {
                throw new IllegalStateException(message);
            }
        }
    }
}
