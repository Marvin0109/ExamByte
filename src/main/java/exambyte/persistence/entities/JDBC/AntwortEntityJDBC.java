package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("antwort")
public class AntwortEntityJDBC {

    @Id
    private Long id;

    @Column("antwort_text")
    private String antwortText;

    private boolean istKorrekt;

    @Column("frage_antwort_id")
    private Long frageId;

    @Column("student_id")
    private Long studentId;

    public AntwortEntityJDBC() {
        this.antwortText = "";
        this.istKorrekt = false;
    }

    public AntwortEntityJDBC(Long id, String antwortText, boolean istKorrekt, Long frageId, Long studentId) {
        this.id = id;
        this.antwortText = antwortText;
        this.istKorrekt = istKorrekt;
        this.frageId = frageId;
        this.studentId = studentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public void setAntwortText(String antwortText) {
        this.antwortText = antwortText;
    }

    public boolean getIstKorrekt() {
        return istKorrekt;
    }

    public void setIstKorrekt(boolean istKorrekt) {
        this.istKorrekt = istKorrekt;
    }

    public Long getFrageId() {
        return frageId;
    }

    public void setFrageId(Long frageId) {
        this.frageId = frageId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
