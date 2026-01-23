package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("antwort")
public class AntwortEntity {

    @Id
    private Long id;

    @Column("antwort_text")
    private final String antwortText;

    @Column("fach_id")
    private final UUID fachId;

    @Column("frage_antwort_id")
    private final UUID frageFachId;

    @Column("student_fach_id")
    private final UUID studentFachId;

    @Column("antwort_zeitpunkt")
    private final LocalDateTime antwortZeitpunkt;

    private AntwortEntity(UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId,
                          LocalDateTime antwortZeitpunkt) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachId;
        this.antwortZeitpunkt = antwortZeitpunkt;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public UUID getFrageFachId() {
        return frageFachId;
    }

    public UUID getStudentFachId() {
        return studentFachId;
    }

    public LocalDateTime getAntwortZeitpunkt() {
        return antwortZeitpunkt;
    }

    public static class AntwortEntityBuilder {
        private UUID fachId;
        private UUID frageFachId;
        private String antwortText;
        private UUID studentFachId;
        private LocalDateTime antwortZeitpunkt;

        public AntwortEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public AntwortEntityBuilder frageFachId(UUID frageFachId) {
            this.frageFachId = frageFachId;
            return this;
        }

        public AntwortEntityBuilder studentFachId(UUID studentFachId) {
            this.studentFachId = studentFachId;
            return this;
        }

        public AntwortEntityBuilder antwortText(String antwortText) {
            this.antwortText = antwortText;
            return this;
        }

        public AntwortEntityBuilder antwortZeitpunkt(LocalDateTime antwortZeitpunkt) {
            this.antwortZeitpunkt = antwortZeitpunkt;
            return this;
        }

        public AntwortEntity build() {
            if (antwortText == null || frageFachId == null || studentFachId == null) {
                throw new IllegalStateException("Alle erforderlichen Felder müssen gesetzt werden (exklusive der Id).");
            }
            return new AntwortEntity(fachId, antwortText, frageFachId, studentFachId,  antwortZeitpunkt);
        }
    }
}
