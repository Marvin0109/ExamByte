package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("exam")
public class ExamEntity {

    @Id
    private UUID id;

    @Column("title")
    private String title;

    @Column("professor_id")
    private final UUID professorId;

    @Column("start_time")
    private final LocalDateTime startZeitpunkt;

    @Column("end_time")
    private final LocalDateTime endZeitpunkt;

    @Column("result_time")
    private final LocalDateTime resultZeitpunkt;

    private ExamEntity(UUID id, String title, UUID professorId,
                      LocalDateTime startZeitpunkt, LocalDateTime endZeitpunkt, LocalDateTime resultZeitpunkt) {
        this.id = id;
        this.title = title;
        this.professorId = professorId;
        this.startZeitpunkt = startZeitpunkt;
        this.endZeitpunkt = endZeitpunkt;
        this.resultZeitpunkt = resultZeitpunkt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getProfessorId() {
        return professorId;
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
        private UUID id;
        private String title;
        private UUID professorId;
        private LocalDateTime startZeitpunkt;
        private LocalDateTime endZeitpunkt;
        private LocalDateTime resultZeitpunkt;

        public ExamEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ExamEntityBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ExamEntityBuilder professorId(UUID professorId) {
            this.professorId = professorId;
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
            if (title == null || title.isBlank()) {
                throw new IllegalStateException("Titel fehlt");
            }

            checkNotNull(professorId, "Professor-ID fehlt");
            checkNotNull(startZeitpunkt, "Start-Zeitpunkt fehlt");
            checkNotNull(endZeitpunkt, "End-Zeitpunkt fehlt");
            checkNotNull(resultZeitpunkt, "Ergebnis-Zeitpunkt fehlt");
            return new ExamEntity(id, title, professorId, startZeitpunkt, endZeitpunkt, resultZeitpunkt);
        }

        private static void checkNotNull(Object object, String message) {
            if (object == null) {
                throw new IllegalStateException(message);
            }
        }
    }
}
