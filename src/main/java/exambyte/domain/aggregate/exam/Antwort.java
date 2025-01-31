package exambyte.domain.aggregate.exam;

import java.util.UUID;

public class Antwort {

    private final Long id;
    private final UUID fachId;
    private final String antwortText;
    private boolean istKorrekt;
    private final UUID frageFachId;
    private final UUID studentFachId;

    private Antwort(Long id, UUID fachId, String antwortText, boolean istKorrekt, UUID frageFachId, UUID studentFachID) {
        this.fachId = fachId;
        this.id = id;
        this.antwortText = antwortText;
        this.istKorrekt = istKorrekt;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachID;
    }

    // Factory Methode
    public static Antwort of(Long id, UUID fachId, String antwortText, boolean istKorrekt, UUID frageFachId,
                             UUID studentFachId) {
        return new Antwort(id, fachId != null ? fachId : UUID.randomUUID(), antwortText, istKorrekt, frageFachId,
                studentFachId);
    }

    public void setIstKorrekt(boolean istKorrekt) {
        this.istKorrekt = istKorrekt;
    }

    public boolean getIstKorrekt() {
        return istKorrekt;
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
}
