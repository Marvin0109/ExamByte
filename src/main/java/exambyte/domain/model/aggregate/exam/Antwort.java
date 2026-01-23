package exambyte.domain.model.aggregate.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Antwort {

    private final UUID fachId;
    private final String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;
    private final LocalDateTime antwortZeitpunkt;

    private Antwort(UUID fachId, String antwortText, UUID frageFachId, UUID studentFachID,
                    LocalDateTime antwortZeitpunkt) {
        this.fachId = fachId;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachID;
        this.antwortZeitpunkt = antwortZeitpunkt;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public UUID getFrageFachId() {
        return frageFachId;
    }

    public UUID getStudentUUID() {
        return studentFachId;
    }
    public UUID getFachId() { return fachId; }

    public LocalDateTime getAntwortZeitpunkt() { return antwortZeitpunkt; }

    public static class AntwortBuilder {
        private UUID fachId;
        private String antwortText;
        private UUID frageFachId;
        private UUID studentFachId;
        private LocalDateTime antwortZeitpunkt;

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

        public Antwort build() {
            return new Antwort(fachId, antwortText, frageFachId, studentFachId,  antwortZeitpunkt);
        }
    }
}
