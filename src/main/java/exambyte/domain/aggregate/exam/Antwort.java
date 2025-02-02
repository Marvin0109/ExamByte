package exambyte.domain.aggregate.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Antwort {

    private final Long id;
    private final UUID fachId;
    private String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;
    private final LocalDateTime antwortZeitpunkt;
    private LocalDateTime lastChangesZeitpunkt;

    private Antwort(Long id, UUID fachId, String antwortText, UUID frageFachId, UUID studentFachID,
                    LocalDateTime antwortZeitpunkt, LocalDateTime lastChangesZeitpunkt) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachID;
        this.antwortZeitpunkt = antwortZeitpunkt;
        this.lastChangesZeitpunkt = lastChangesZeitpunkt;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public UUID getFrageFachId() {
        return frageFachId;
    }

    public Long getId() {
        return id;
    }
    public UUID getStudentUUID() {
        return studentFachId;
    }
    public UUID getFachId() { return fachId; }

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

    public static class AntwortBuilder {
        private Long id;
        private UUID fachId;
        private String antwortText;
        private UUID frageFachId;
        private UUID studentFachId;
        private LocalDateTime antwortZeitpunkt;
        private LocalDateTime lastChangesZeitpunkt;

        public AntwortBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AntwortBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public AntwortBuilder antwortText(String antwortText) {
            this.antwortText = antwortText;
            return this;
        }

        public AntwortBuilder frageFachId(UUID frageFachId) {
            this.frageFachId = frageFachId;
            return this;
        }

        public AntwortBuilder studentFachId(UUID studentFachId) {
            this.studentFachId = studentFachId;
            return this;
        }

        public AntwortBuilder antwortZeitpunkt(LocalDateTime antwortZeitpunkt) {
            this.antwortZeitpunkt = antwortZeitpunkt;
            return this;
        }

        public AntwortBuilder lastChangesZeitpunkt(LocalDateTime lastChangesZeitpunkt) {
            this.lastChangesZeitpunkt = lastChangesZeitpunkt;
            return this;
        }

        public Antwort build() {
            return new Antwort(id, fachId, antwortText, frageFachId, studentFachId, antwortZeitpunkt, lastChangesZeitpunkt);
        }
    }
}
