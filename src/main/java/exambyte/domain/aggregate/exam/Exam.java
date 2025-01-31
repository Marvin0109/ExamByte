package exambyte.domain.aggregate.exam;

import java.util.*;

public class Test {

    private final Long id;
    private final UUID fachId;
    private final String title;
    private final UUID professorFachId;

    private Test(Long id, UUID fachId, String title, UUID professorFachId) {
        this.fachId = fachId;
        this.id = id;
        this.title = title;
        this.professorFachId = professorFachId;
    }

    public static Test of(Long id, UUID fachId, String title, UUID professorFachId) {
        return new Test(id, fachId != null ? fachId : UUID.randomUUID(), title, professorFachId);
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
