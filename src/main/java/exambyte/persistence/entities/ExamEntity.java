package exambyte.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("exam")
public class ExamEntity {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("title")
    private String title;

    @Column("professor_fach_id")
    private final UUID professorFachId;

    @Column("start_time")
    private final LocalDateTime startZeitpunkt;

    @Column("end_time")
    private final LocalDateTime endZeitpunkt;

    @Column("result_time")
    private final LocalDateTime resultZeitpunkt;

    public ExamEntity(Long id, UUID fachId, String title, UUID professorFachId,
                      LocalDateTime startZeitpunkt, LocalDateTime endZeitpunkt, LocalDateTime resultZeitpunkt) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.title = title;
        this.professorFachId = professorFachId;
        this.startZeitpunkt = startZeitpunkt;
        this.endZeitpunkt = endZeitpunkt;
        this.resultZeitpunkt = resultZeitpunkt;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getProfessorFachId() {
        return professorFachId;
    }

    public LocalDateTime getStartZeitpunkt() {
        return startZeitpunkt;
    }

    public LocalDateTime getEndZeitpunkt() {
        return endZeitpunkt;
    }

    public LocalDateTime getResultZeitpunkt() {
        return resultZeitpunkt;
    }
}
