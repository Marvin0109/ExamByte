package exambyte.persistence.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "antwort")
public class AntwortEntity {

    @Id
    private Long id;
    private String AntwortText;
    private boolean istKorrekt;

    @ManyToOne
    @JoinColumn(name = "frage_antwort_id", foreignKey = @ForeignKey(name = "FK_FRAGE_ID"))
    private FrageEntity frage;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "FK_STUDENT_ID"))
    private StudentEntity student;

    public AntwortEntity() {
        this.AntwortText = "";
        this.istKorrekt = false;
    }

    @PersistenceCreator
    public AntwortEntity(Long id, String AntwortText, boolean istKorrekt, FrageEntity frage, StudentEntity student) {
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

    public FrageEntity getFrage() {
        return frage;
    }

    public Long getId() {
        return id;
    }
    public StudentEntity getStudent() {
        return student;
    }
}
