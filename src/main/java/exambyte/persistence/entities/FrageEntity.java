package exambyte.persistence.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "frage")
public class FrageEntity {

    @Id
    private final Long id;
    private final String frageText;

    @ManyToOne
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(name = "FK_PROFESSOR_ID"))
    private ProfessorEntity professor;

    public FrageEntity() {
        this.id = null;
        this.frageText = "";
    }

    @PersistenceCreator
    public FrageEntity(Long id, String frageText, ProfessorEntity professor) {
        this.id = id;
        this.frageText = frageText;
        this.professor = professor;
    }

    public ProfessorEntity getProfessor() {
        return professor;
    }

    public String getFrageText() {
        return frageText;
    }

    public Long getId() {
        return id;
    }
}
