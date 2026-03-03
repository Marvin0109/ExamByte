package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("antwort")
public class AntwortEntity {

    @Id
    private UUID id;

    @Column("antwort_text")
    private String antwortText;

    @Column("frage_id")
    private final UUID frageId;

    @Column("student_id")
    private final UUID studentId;

    @Column("antwort_zeitpunkt")
    private final LocalDateTime antwortZeitpunkt;

    private AntwortEntity(UUID id, String antwortText, UUID frageId, UUID studentId,
                          LocalDateTime antwortZeitpunkt) {
        this.id = id;
        this.antwortText = antwortText;
        this.frageId = frageId;
        this.studentId = studentId;
        this.antwortZeitpunkt = antwortZeitpunkt;
    }

    public UUID getId() {
        return id;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public void setAntwortText(String antwortText) {
        this.antwortText = antwortText;
    }

    public UUID getFrageId() {
        return frageId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public LocalDateTime getAntwortZeitpunkt() {
        return antwortZeitpunkt;
    }

    public static class AntwortEntityBuilder {
        private UUID id;
        private UUID frageId;
        private String antwortText;
        private UUID studentId;
        private LocalDateTime antwortZeitpunkt;

        public AntwortEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AntwortEntityBuilder frageId(UUID frageId) {
            this.frageId = frageId;
            return this;
        }

        public AntwortEntityBuilder studentId(UUID studentId) {
            this.studentId = studentId;
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
            if (antwortText == null || antwortText.isBlank()) {
                throw new IllegalStateException("Antworttext fehlt");
            }
            if (frageId == null) {
                throw new IllegalStateException("Frage-ID fehlt");
            }
            if (studentId == null) {
                throw new IllegalStateException("Student-ID fehlt");
            }
            return new AntwortEntity(id, antwortText, frageId, studentId,  antwortZeitpunkt);
        }
    }
}
