package exambyte.domain.aggregate.test;

import exambyte.domain.aggregate.user.Professor;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Frage {
    @Id
    private final int id;
    private final String[] frageText;
    @ManyToOne
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(name = "FK_PROFESSOR_ID"))
    private Professor professor;

    private Frage(int id, String[] frageText, Professor professor) {
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

    public int getId() {
        return id;
    }

    public Frage getFrage() {
        return new Frage(id, frageText, professor);
    }
}
