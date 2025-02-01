package exambyte.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("frage")
public class FrageEntity {

    @Id
    private final Long id;

    @Column("frage_text")
    private String frageText;

    @Column("fach_id")
    private final UUID fachId;

    @Column("professor_fach_id")
    private final UUID professorFachId;

    @Column("exam_fach_id")
    private final UUID examFachId;

    @Column("max_punkte")
    private int maxPunkte;

    public FrageEntity(Long id, UUID fachId, String frageText, int maxPunkte, UUID professorFachId, UUID examFachId) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.professorFachId = professorFachId;
        this.examFachId = examFachId;
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

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }

    public UUID getProfessorFachId() {
        return professorFachId;
    }

    public UUID getExamFachId() {
        return examFachId;
    }
}
