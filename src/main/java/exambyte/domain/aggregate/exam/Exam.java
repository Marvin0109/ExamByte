package exambyte.domain.aggregate.exam;

import java.util.UUID;

public class Exam {

    private final Long id;
    private final UUID fachId;
    private final String title;
    private final UUID professorFachId;

    private Exam(Long id, UUID fachId, String title, UUID professorFachId) {
        this.fachId = fachId;
        this.id = id;
        this.title = title;
        this.professorFachId = professorFachId;
    }

    public static Exam of(Long id, UUID fachId, String title, UUID professorFachId) {
        return new Exam(id, fachId != null ? fachId : UUID.randomUUID(), title, professorFachId);
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getTitle() {
        return title;
    }

    public UUID getProfessorFachId() {
        return professorFachId;
    }

    // Getter und Methoden für das Arbeiten mit Fragen und Antworten
}
