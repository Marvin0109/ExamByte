package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AntwortDTO {

    private final UUID fachId;
    private final String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;
    private final LocalDateTime antwortZeitpunkt;

    private AntwortDTO(UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId,
                       LocalDateTime antwortZeitpunkt) {
        this.fachId = fachId;
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


    public static class AntwortDTOBuilder {
        private UUID fachId;
        private String antwortText;
        private UUID frageFachId;
        private UUID studentFachId;
        private LocalDateTime antwortZeitpunkt;

        public AntwortDTOBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public AntwortDTOBuilder antwortText(String antwortText) {
            this.antwortText = antwortText;
            return this;
        }

        public AntwortDTOBuilder frageFachId(UUID frageFachId) {
            this.frageFachId = frageFachId;
            return this;
        }

        public AntwortDTOBuilder studentFachId(UUID studentFachId) {
            this.studentFachId = studentFachId;
            return this;
        }

        public AntwortDTOBuilder antwortZeitpunkt(LocalDateTime antwortZeitpunkt) {
            this.antwortZeitpunkt = antwortZeitpunkt;
            return this;
        }

        public AntwortDTO build() {
            return new AntwortDTO(fachId, antwortText, frageFachId, studentFachId,  antwortZeitpunkt);
        }
    }
}
