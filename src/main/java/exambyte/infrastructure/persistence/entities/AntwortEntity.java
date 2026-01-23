package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

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

    private AntwortEntity(UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachId;
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

    public static class AntwortEntityBuilder {
        private UUID fachId;
        private UUID frageFachId;
        private String antwortText;
        private UUID studentFachId;

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

        public AntwortEntity build() {
            if (antwortText == null || frageFachId == null || studentFachId == null) {
                throw new IllegalStateException("Alle erforderlichen Felder müssen gesetzt werden (exklusive der Id).");
            }
            return new AntwortEntity(fachId, antwortText, frageFachId, studentFachId);
        }
    }
}
