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
        this.fachId = fachId;
        this.id = id;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachID;
        this.antwortZeitpunkt = antwortZeitpunkt;
        this.lastChangesZeitpunkt = lastChangesZeitpunkt;
    }

    // Factory Methode
    public static Antwort of(Long id, UUID fachId, String antwortText,
                             UUID frageFachId, UUID studentFachId,
                             LocalDateTime antwortDatum, LocalDateTime lastChangesDatum) {
        return new Antwort(id, fachId != null ? fachId : UUID.randomUUID(),
                antwortText, frageFachId, studentFachId,  antwortDatum, lastChangesDatum);
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
}
