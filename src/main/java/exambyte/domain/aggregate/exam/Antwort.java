package exambyte.domain.aggregate.exam;

import exambyte.domain.aggregate.user.Student;

import java.util.UUID;

public class Antwort {

    private final Long id;
    private final UUID uuid;
    private final String antwortText;
    private boolean istKorrekt;
    private final Frage frage;
    private final UUID studentFachId;

    private Antwort(Long id, UUID uuid,String antwortText, boolean istKorrekt, Frage frage, UUID studentFachID) {
        this.uuid = uuid;
        this.id = id;
        this.antwortText = antwortText;
        this.istKorrekt = istKorrekt;
        this.frage = frage;
        this.studentFachId = studentFachID;
    }

    // Factory Methode
    public static Antwort of(Long id, UUID uuid, String antwortText, boolean istKorrekt, Frage frage, UUID studentFachId) {
        return new Antwort(id, uuid, antwortText, istKorrekt, frage, studentFachId);
    }

    public Antwort getAntwort() {
        return new Antwort(id, uuid, antwortText, istKorrekt, frage, studentFachId);
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

    public Frage getFrage() {
        return frage;
    }

    public Long getId() {
        return id;
    }
    public UUID getStudentUUID() {
        return studentFachId;
    }
    public UUID getUuid() { return uuid; }
}
