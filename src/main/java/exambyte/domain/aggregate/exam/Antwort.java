package exambyte.domain.aggregate.exam;

import java.util.UUID;

public class Antwort {

    private final Long id;
    private final UUID fachId;
    private final String antwortText;
    private final UUID frageFachId;
    private final UUID studentFachId;

    private Antwort(Long id, UUID fachId, String antwortText, UUID frageFachId, UUID studentFachID) {
        this.fachId = fachId;
        this.id = id;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachID;
    }

    // Factory Methode
    public static Antwort of(Long id, UUID fachId, String antwortText, UUID frageFachId,
                             UUID studentFachId) {
        return new Antwort(id, fachId != null ? fachId : UUID.randomUUID(), antwortText, frageFachId,
                studentFachId);
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
