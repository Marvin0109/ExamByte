package exambyte.domain.aggregate.exam;

import exambyte.domain.aggregate.user.Student;

public class Antwort {

    private final Long id;
    private final String antwortText;
    private boolean istKorrekt;
    private final Frage frage;
    private final Student student;

    private Antwort(Long id, String antwortText, boolean istKorrekt, Frage frage, Student student) {
        this.id = id;
        this.antwortText = antwortText;
        this.istKorrekt = istKorrekt;
        this.frage = frage;
        this.student = student;
    }

    // Factory Methode
    public static Antwort of(Long id, String antwortText, boolean istKorrekt, Frage frage, Student student) {
        return new Antwort(id, antwortText, istKorrekt, null, student);
    }

    public Antwort getAntwort() {
        return new Antwort(id, antwortText, istKorrekt, frage, student);
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
    public Student getStudent() {
        return student;
    }
}
