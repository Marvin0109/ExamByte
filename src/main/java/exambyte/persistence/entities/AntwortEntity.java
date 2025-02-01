package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Table("antwort")
public class AntwortEntityJDBC {

    @Id
    private Long id;

    @Column("antwort_text")
    private String antwortText;

    @Column("fach_id")
    private final UUID fachId;

    @Column("frage_antwort_id")
    private final UUID frageFachId;

    @Column("student_fach_id")
    private final UUID studentFachId;

    public AntwortEntityJDBC(Long id, UUID fachId, String antwortText, UUID frageFachId, UUID studentFachId) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.antwortText = antwortText;
        this.frageFachId = frageFachId;
        this.studentFachId = studentFachId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getAntwortText() {
        return antwortText;
    }

    public void setAntwortText(String antwortText) {
        this.antwortText = antwortText;
    }

    public UUID getFrageFachId() {
        return frageFachId;
    }

    public UUID getStudentFachId() {
        return studentFachId;
    }
}
