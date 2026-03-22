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
    private final LocalDateTime start;

    @Column("end_time")
    private final LocalDateTime end;

    @Column("result_time")
    private final LocalDateTime result;

    private ExamEntity(UUID id, String title, UUID professorId,
                       LocalDateTime start, LocalDateTime end, LocalDateTime result) {
        this.id = id;
        this.title = title;
        this.professorId = professorId;
        this.start = start;
        this.end = end;
        this.result = result;
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

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDateTime getResult() {
        return result;
    }

    public static class ExamEntityBuilder {
        private UUID id;
        private String title;
        private UUID professorId;
        private LocalDateTime start;
        private LocalDateTime end;
        private LocalDateTime result;

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

        public ExamEntityBuilder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public ExamEntityBuilder end(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public ExamEntityBuilder result(LocalDateTime result) {
            this.result = result;
            return this;
        }

        public ExamEntity build() {
            if (title == null || title.isBlank()) {
                throw new IllegalStateException("Titel fehlt");
            }

            checkNotNull(professorId, "Professor-ID fehlt");
            checkNotNull(start, "Start-Zeitpunkt fehlt");
            checkNotNull(end, "End-Zeitpunkt fehlt");
            checkNotNull(result, "Ergebnis-Zeitpunkt fehlt");
            return new ExamEntity(id, title, professorId, start, end, result);
        }

        private static void checkNotNull(Object object, String message) {
            if (object == null) {
                throw new IllegalStateException(message);
            }
        }
    }
}
