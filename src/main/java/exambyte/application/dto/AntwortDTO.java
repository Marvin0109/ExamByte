package exambyte.application.dto;

import java.util.UUID;

public class AntwortDTO {

    private final UUID fachId;
    private String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;

    private AntwortDTO(UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId) {
        this.fachId = fachId;
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


    public static class AntwortDTOBuilder {
        private UUID fachId;
        private String antwortText;
        private UUID frageFachId;
        private UUID studentFachId;

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

        public AntwortDTO build() {
            return new AntwortDTO(fachId, antwortText, frageFachId, studentFachId);
        }
    }
}
