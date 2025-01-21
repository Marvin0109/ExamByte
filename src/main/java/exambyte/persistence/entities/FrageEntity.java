package exambyte.persistence.entities;

import exambyte.domain.aggregate.user.Professor;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.PersistenceCreator;

public class FrageEntity {

    @Id
    private final Integer id;
    private final String[] frageText;

    @ManyToOne
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(name = "FK_PROFESSOR_ID"))
    private final Professor professor;

    @PersistenceCreator
    public FrageEntity(Integer id, String[] frageText, Professor professor) {
        this.id = id;
        this.frageText = frageText;
        this.professor = professor;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String[] getFrageText() {
        return frageText;
    }

    public Integer getId() {
        return id;
    }
}
