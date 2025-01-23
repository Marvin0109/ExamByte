package exambyte.persistence.entities;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.PersistenceCreator;

public class AntwortEntity {

    @Id
    private final int id;
    private final String[] AntwortText;
    private boolean istKorrekt;

    @ManyToOne
    @JoinColumn(name = "frage_id", foreignKey = @ForeignKey(name = "FK_FRAGE_ID"))
    private final Frage frage;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "FK_STUDENT_ID"))
    private final Student student;

    @PersistenceCreator
    public AntwortEntity(int id, String[] AntwortText, boolean istKorrekt, Frage frage, Student student) {
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

    public String[] getAntwortText() {
        return AntwortText;
    }

    public Frage getFrage() {
        return frage;
    }

    public int getId() {
        return id;
    }
    public Student getStudent() {
        return student;
    }
}
