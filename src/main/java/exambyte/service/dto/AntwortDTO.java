package exambyte.service.dto;

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

    public AntwortDTO(Long id, UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId,
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
}
