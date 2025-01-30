package exambyte.domain.aggregate.exam;

import exambyte.domain.aggregate.user.Professor;

import java.util.UUID;

public class Frage {

    private final Long id;
    private final UUID uuid;
    private final String frageText;
    private final UUID professorUUID;

    private Frage(Long id, UUID uuid,String frageText, UUID professorUUID) {
        this.uuid = uuid;
        this.id = id;
        this.frageText = frageText;
        this.professorUUID = professorUUID;
    }

    // Factory Methode
    public static Frage of(Long id, UUID uuid, String frageText, UUID professorUUID) {
        return new Frage(id, uuid, frageText, professorUUID);
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

    public UUID getUuid() {
        return uuid;
    }
}
