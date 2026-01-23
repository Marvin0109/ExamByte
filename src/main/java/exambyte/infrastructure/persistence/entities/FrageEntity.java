package exambyte.infrastructure.persistence.entities;

import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("frage")
public class FrageEntity {

    @Id
    private Long id;

    @Column("frage_text")
    private String frageText;

    @Column("fach_id")
    private final UUID fachId;

    @Column("type")
    private final QuestionTypeEntity type;

    @Column("professor_fach_id")
    private final UUID professorFachId;

    @Column("exam_fach_id")
    private final UUID examFachId;

    @Column("max_punkte")
    private int maxPunkte;

    private FrageEntity(UUID fachId, String frageText, int maxPunkte, QuestionTypeEntity type,
                        UUID professorFachId, UUID examFachId) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.type = type;
        this.professorFachId = professorFachId;
        this.examFachId = examFachId;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }

    public QuestionTypeEntity getType() {
        return type;
    }

    public UUID getProfessorFachId() {
        return professorFachId;
    }

    public UUID getExamFachId() {
        return examFachId;
    }

    public static class FrageEntityBuilder {
        private UUID fachId;
        private String frageText;
        private int maxPunkte;
        private QuestionTypeEntity type;
        private UUID professorFachId;
        private UUID examFachId;

        public FrageEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public FrageEntityBuilder frageText(String frageText) {
            this.frageText = frageText;
            return this;
        }

        public FrageEntityBuilder maxPunkte(int maxPunkte) {
            this.maxPunkte = maxPunkte;
            return this;
        }

        public FrageEntityBuilder type(QuestionTypeEntity type) {
            this.type = type;
            return this;
        }

        public FrageEntityBuilder professorFachId(UUID professorFachId) {
            this.professorFachId = professorFachId;
            return this;
        }

        public FrageEntityBuilder examFachId(UUID examFachId) {
            this.examFachId = examFachId;
            return this;
        }

        public FrageEntity build() {
            if (frageText == null || maxPunkte <= 0 || professorFachId == null || examFachId == null) {
                throw new IllegalStateException("Alle Felder müssen gesetzt werden (außer die Id).");
            }
            return new FrageEntity(fachId, frageText, maxPunkte, type, professorFachId, examFachId);
        }
    }
}
