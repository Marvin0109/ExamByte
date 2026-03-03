package exambyte.domain.model.aggregate.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Antwort {

    private final UUID id;
    private final String antwortText;
    private final UUID frageId;
    private final UUID studentId;
    private final LocalDateTime antwortZeitpunkt;

    private Antwort(UUID id, String antwortText, UUID frageId, UUID studentId,
                    LocalDateTime antwortZeitpunkt) {
        this.id = id;
        this.antwortText = antwortText;
        this.frageId = frageId;
        this.studentId = studentId;
        this.antwortZeitpunkt = antwortZeitpunkt;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public UUID getFrageId() {
        return frageId;
    }

    public UUID getStudentUUID() {
        return studentId;
    }
    public UUID getId() { return id; }

    public LocalDateTime getAntwortZeitpunkt() { return antwortZeitpunkt; }

    public static class AntwortBuilder {
        private UUID id;
        private String antwortText;
        private UUID frageId;
        private UUID studentId;
        private LocalDateTime antwortZeitpunkt;

        public AntwortBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AntwortBuilder antwortText(String antwortText) {
            this.antwortText = antwortText;
            return this;
        }

        public AntwortBuilder frageId(UUID frageId) {
            this.frageId = frageId;
            return this;
        }

        public AntwortBuilder studentId(UUID studentId) {
            this.studentId = studentId;
            return this;
        }

        public AntwortBuilder antwortZeitpunkt(LocalDateTime antwortZeitpunkt) {
            this.antwortZeitpunkt = antwortZeitpunkt;
            return this;
        }

        public Antwort build() {
            return new Antwort(id, antwortText, frageId, studentId,  antwortZeitpunkt);
        }
    }
}
