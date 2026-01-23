package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class Antwort {

    private final UUID fachId;
    private String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;

    private Antwort(UUID fachId, String antwortText, UUID frageFachId, UUID studentFachID) {
        this.fachId = fachId;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachID;
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

    public void updateAntwortText(String newAntwortText) {
        this.antwortText = newAntwortText;
    }

    public static class AntwortBuilder {
        private UUID fachId;
        private String antwortText;
        private UUID frageFachId;
        private UUID studentFachId;

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

        public Antwort build() {
            return new Antwort(fachId, antwortText, frageFachId, studentFachId);
        }
    }
}
