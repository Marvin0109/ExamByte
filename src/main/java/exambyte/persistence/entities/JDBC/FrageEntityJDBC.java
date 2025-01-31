package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("frage")
public class FrageEntityJDBC {

    @Id
    private final Long id;

    @Column("frage_text")
    private final String frageText;

    @Column("fach_id")
    private final UUID fachId;

    @Column("professor_fach_id")
    private final UUID professorFachId;

    public FrageEntityJDBC(Long id, UUID fachId, String frageText, UUID professorFachId) {
        this.id = id;
        this.fachId = fachId;
        this.frageText = frageText;
        this.professorFachId = professorFachId;
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

    public UUID getProfessorFachId() {
        return professorFachId;
    }
}
