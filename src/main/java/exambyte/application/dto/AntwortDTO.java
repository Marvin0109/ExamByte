package exambyte.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AntwortDTO {

    private final Long id;
    private final UUID fachId;
    private String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;
    private final LocalDateTime antwortZeitpunkt;
    private LocalDateTime lastChangesZeitpunkt;

    private AntwortDTO(Long id, UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId,
                      LocalDateTime antwortZeitpunkt, LocalDateTime lastChangesZeitpunkt) {
        this.id = id;
        this.fachId = fachId;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachId;
        this.antwortZeitpunkt = antwortZeitpunkt;
        this.lastChangesZeitpunkt = lastChangesZeitpunkt;
    }

    public Long getId() {
        return id;
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

    public void setLastChangesZeitpunkt(LocalDateTime lastChangesZeitpunkt) {
        this.lastChangesZeitpunkt = lastChangesZeitpunkt;
    }

    public static class AntwortDTOBuilder {
        private Long id;
        private UUID fachId;
        private String antwortText;
        private UUID frageFachId;
        private UUID studentFachId;
        private LocalDateTime antwortZeitpunkt;
        private LocalDateTime lastChangesZeitpunkt;

        public AntwortDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

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

        public AntwortDTOBuilder lastChangesZeitpunkt(LocalDateTime lastChangesZeitpunkt) {
            this.lastChangesZeitpunkt = lastChangesZeitpunkt;
            return this;
        }

        public AntwortDTO build() {
            return new AntwortDTO(id, fachId, antwortText, frageFachId, studentFachId, antwortZeitpunkt, lastChangesZeitpunkt);
        }
    }
}
