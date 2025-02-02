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
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.professorUUID = professorUUID;
        this.examUUID = examUUID;
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

    public static class FrageBuilder {
        private Long id;
        private UUID fachId;
        private String frageText;
        private int maxPunkte;
        private UUID professorUUID;
        private UUID examUUID;

        public FrageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FrageBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public FrageBuilder frageText(String frageText) {
            this.frageText = frageText;
            return this;
        }

        public FrageBuilder maxPunkte(int maxPunkte) {
            this.maxPunkte = maxPunkte;
            return this;
        }

        public FrageBuilder professorUUID(UUID professorUUID) {
            this.professorUUID = professorUUID;
            return this;
        }

        public FrageBuilder examUUID(UUID examUUID) {
            this.examUUID = examUUID;
            return this;
        }

        public Frage build() {
            return new Frage(id, fachId, frageText, maxPunkte, professorUUID, examUUID);
        }
    }
}
