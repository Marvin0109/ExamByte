package exambyte.persistence.entities.JPA;

import jakarta.persistence.*;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "antwort")
public class AntwortEntityJPA {

    @Id
    private Long id;
    private String AntwortText;
    private boolean istKorrekt;

    @ManyToOne
    @JoinColumn(name = "frage_antwort_id", foreignKey = @ForeignKey(name = "FK_FRAGE_ID"))
    private FrageEntityJPA frage;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "FK_STUDENT_ID"))
    private StudentEntityJPA student;

    public AntwortEntityJPA() {
        this.AntwortText = "";
        this.istKorrekt = false;
    }

    @PersistenceCreator
    public AntwortEntityJPA(Long id, String AntwortText, boolean istKorrekt, FrageEntityJPA frage, StudentEntityJPA student) {
        this.id = id;
        this.AntwortText = AntwortText;
        this.istKorrekt = istKorrekt;
        this.frage = frage;
        this.student = student;
    }

    public void setIstKorrekt(boolean istKorrekt) {
        this.istKorrekt = istKorrekt;
    }

    public boolean getIstKorrekt() {
        return istKorrekt;
    }

    public String getAntwortText() {
        return AntwortText;
    }

    public FrageEntityJPA getFrage() {
        return frage;
    }

    public Long getId() {
        return id;
    }
    public StudentEntityJPA getStudent() {
        return student;
    }
}
