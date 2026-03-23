package exambyte.domain.model.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Exam {

    private final UUID id;
    private final String title;
    private final UUID professorId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final LocalDateTime result;

    private Exam(UUID id, String title, UUID professorId,
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

    public static class ExamBuilder {
        private UUID id;
        private String title;
        private UUID professorId;
        private LocalDateTime start;
        private LocalDateTime end;
        private LocalDateTime result;

        public ExamBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ExamBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ExamBuilder professorId(UUID professorId) {
            this.professorId = professorId;
            return this;
        }

        public ExamBuilder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public ExamBuilder end(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public ExamBuilder result(LocalDateTime result) {
            this.result = result;
            return this;
        }

        public Exam build() {
            return new Exam(id, title, professorId, start, end, result);
        }
    }
}
