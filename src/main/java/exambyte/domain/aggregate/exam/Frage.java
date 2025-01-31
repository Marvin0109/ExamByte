package exambyte.domain.aggregate.exam;

import java.util.UUID;

public class Frage {

    private final Long id;
    private final UUID fachId;
    private final String frageText;
    private final UUID professorUUID;
    private final UUID testUUID;

    private Frage(Long id, UUID fachId,String frageText, UUID professorUUID, UUID testUUID) {
        this.fachId = fachId;
        this.id = id;
        this.frageText = frageText;
        this.professorUUID = professorUUID;
        this.testUUID = testUUID;
    }

    // Factory Methode
    public static Frage of(Long id, UUID fachId, String frageText, UUID professorUUID, UUID testUUID) {
        return new Frage(id, fachId != null ? fachId : UUID.randomUUID(), frageText, professorUUID, testUUID);
    }

    public UUID getProfessorUUID() {
        return professorUUID;
    }

    public String getFrageText() {
        return frageText;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public UUID getTestUUID() {
        return testUUID;
    }
}
