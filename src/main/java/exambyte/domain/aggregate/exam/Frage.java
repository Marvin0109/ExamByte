package exambyte.domain.aggregate.exam;

import java.util.UUID;

public class Frage {

    private final Long id;
    private final UUID fachId;
    private final String frageText;
    private int maxPunkte;
    private final UUID professorUUID;
    private final UUID examUUID;

    private Frage(Long id, UUID fachId,String frageText, int maxPunkte, UUID professorUUID, UUID examUUID) {
        this.fachId = fachId;
        this.id = id;
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.professorUUID = professorUUID;
        this.examUUID = examUUID;
    }

    // Factory Methode
    public static Frage of(Long id, UUID fachId, String frageText, int maxPunkte, UUID professorUUID, UUID testUUID) {
        return new Frage(id, fachId != null ? fachId : UUID.randomUUID(), frageText, maxPunkte, professorUUID, testUUID);
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

    public UUID getExamUUID() {
        return examUUID;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }
}
