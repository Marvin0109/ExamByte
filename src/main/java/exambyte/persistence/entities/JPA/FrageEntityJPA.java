package exambyte.persistence.entities.JPA;

import jakarta.persistence.*;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "frage")
public class FrageEntityJPA {

    @Id
    private final Long id;
    private final String frageText;

    @ManyToOne
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(name = "FK_PROFESSOR_ID"))
    private ProfessorEntityJPA professor;

    public FrageEntityJPA() {
        this.id = null;
        this.frageText = "";
    }

    @PersistenceCreator
    public FrageEntityJPA(Long id, String frageText, ProfessorEntityJPA professor) {
        this.id = id;
        this.frageText = frageText;
        this.professor = professor;
    }

    public ProfessorEntityJPA getProfessor() {
        return professor;
    }

    public String getFrageText() {
        return frageText;
    }

    public Long getId() {
        return id;
    }
}
