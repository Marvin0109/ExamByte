package exambyte.application.dto;

import java.util.UUID;

public class FrageDTO {

    private final Long id;
    private final UUID fachId;
    private final String frageText;
    private int maxPunkte;
    private final UUID professorUUID;
    private final UUID examUUID;

    public FrageDTO(Long id, UUID fachId, String frageText, int maxPunkte, UUID professorUUID, UUID examUUID) {
        this.id = id;
        this.fachId = fachId;
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.professorUUID = professorUUID;
        this.examUUID = examUUID;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getFrageText() {
        return frageText;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }

    public UUID getProfessorUUID() {
        return professorUUID;
    }

    public UUID getExamUUID() {
        return examUUID;
    }
}
