package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("exam")
public class ExamEntity {

    @Id
    private Long id;

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

    private ExamEntity(UUID fachId, String title, UUID professorFachId,
                      LocalDateTime startZeitpunkt, LocalDateTime endZeitpunkt, LocalDateTime resultZeitpunkt) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.title = title;
        this.professorFachId = professorFachId;
        this.startZeitpunkt = startZeitpunkt;
        this.endZeitpunkt = endZeitpunkt;
        this.resultZeitpunkt = resultZeitpunkt;
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

    public static class ExamEntityBuilder {
        private UUID fachId;
        private String title;
        private UUID professorFachId;
        private LocalDateTime startZeitpunkt;
        private LocalDateTime endZeitpunkt;
        private LocalDateTime resultZeitpunkt;

        public ExamEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ExamEntityBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ExamEntityBuilder professorFachId(UUID professorFachId) {
            this.professorFachId = professorFachId;
            return this;
        }

        public ExamEntityBuilder startZeitpunkt(LocalDateTime startZeitpunkt) {
            this.startZeitpunkt = startZeitpunkt;
            return this;
        }

        public ExamEntityBuilder endZeitpunkt(LocalDateTime endZeitpunkt) {
            this.endZeitpunkt = endZeitpunkt;
            return this;
        }

        public ExamEntityBuilder resultZeitpunkt(LocalDateTime resultZeitpunkt) {
            this.resultZeitpunkt = resultZeitpunkt;
            return this;
        }

        public ExamEntity build() {
            if (title == null || professorFachId == null) {
                throw new IllegalArgumentException("Der Titel und die professorFachId müssen gesetzt werden.");
            }
            return new ExamEntity(fachId, title, professorFachId, startZeitpunkt, endZeitpunkt, resultZeitpunkt);
        }
    }
}
