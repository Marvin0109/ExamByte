package exambyte.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("exam")
public class ExamEntity {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("title")
    private String title;

    @Column("professor_fach_id")
    private final UUID professorFachId;

    public ExamEntity(Long id, UUID fachId, String title, UUID professorFachId) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.title = title;
        this.professorFachId = professorFachId;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getProfessorFachId() {
        return professorFachId;
    }
}
