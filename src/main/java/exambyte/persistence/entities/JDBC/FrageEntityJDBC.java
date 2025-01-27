package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import org.springframework.data.relational.core.mapping.Table;

@Table("frage")
public class FrageEntityJDBC {

    @Id
    private final Long id;

    @Column("frage_text")
    private final String frageText;

    @Column("professor_id")
    private final Long professorId;

    public FrageEntityJDBC() {
        this.id = null;
        this.frageText = "";
        this.professorId = null;
    }

    public FrageEntityJDBC(Long id, String frageText, Long professorId) {
        this.id = id;
        this.frageText = frageText;
        this.professorId = professorId;
    }

    public Long getId() {
        return id;
    }

    public String getFrageText() {
        return frageText;
    }

    public Long getProfessorId() {
        return professorId;
    }
}
