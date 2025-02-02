package exambyte.persistence.entities;

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
    private String antwortText;

    @Column("fach_id")
    private final UUID fachId;

    @Column("frage_antwort_id")
    private final UUID frageFachId;

    @Column("student_fach_id")
    private final UUID studentFachId;

    @Column("antwort_zeitpunkt")
    private final LocalDateTime antwortZeitpunkt;

    @Column("last_changes_zeitpunkt")
    private LocalDateTime lastChangesZeitpunkt;

    private AntwortEntity(Long id, UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId,
                         LocalDateTime antwortZeitpunkt, LocalDateTime lastChangesZeitpunkt) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachId;
        this.antwortZeitpunkt = antwortZeitpunkt;
        this.lastChangesZeitpunkt = lastChangesZeitpunkt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public void setAntwortText(String antwortText) {
        this.antwortText = antwortText;
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

    public LocalDateTime getLastChangesZeitpunkt() {
        return lastChangesZeitpunkt;
    }

    public void updateAntwortText(String newAntwortText) {
        this.antwortText = newAntwortText;
        this.lastChangesZeitpunkt = LocalDateTime.now();
    }

    public static class AntwortEntityBuilder {
        private Long id;
        private UUID fachId;
        private UUID frageFachId;
        private String antwortText;
        private UUID studentFachId;
        private LocalDateTime antwortZeitpunkt;
        private LocalDateTime lastChangesZeitpunkt;

        public AntwortEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

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

        public AntwortEntityBuilder lastChangesZeitpunkt(LocalDateTime lastChangesZeitpunkt) {
            this.lastChangesZeitpunkt = lastChangesZeitpunkt;
            return this;
        }

        public AntwortEntity build() {
            if (antwortText == null || frageFachId == null || studentFachId == null) {
                throw new IllegalStateException("Alle erforderlichen Felder müssen gesetzt werden (exklusive der Id).");
            }
            return new AntwortEntity(id, fachId, antwortText, frageFachId, studentFachId, antwortZeitpunkt, lastChangesZeitpunkt);
        }
    }
}
