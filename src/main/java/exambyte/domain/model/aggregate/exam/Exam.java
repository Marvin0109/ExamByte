package exambyte.domain.model.aggregate.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Exam {

    private final Long id;
    private final UUID fachId;
    private final String title;
    private final UUID professorFachId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final LocalDateTime resultTime;

    private Exam(Long id, UUID fachId, String title, UUID professorFachId,
                 LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.title = title;
        this.professorFachId = professorFachId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
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

    public UUID getProfessorFachId() {
        return professorFachId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getResultTime() {
        return resultTime;
    }

    public static class ExamBuilder {
        private Long id;
        private UUID fachId;
        private String title;
        private UUID professorFachId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private LocalDateTime resultTime;

        public ExamBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ExamBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ExamBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ExamBuilder professorFachId(UUID professorFachId) {
            this.professorFachId = professorFachId;
            return this;
        }

        public ExamBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ExamBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ExamBuilder resultTime(LocalDateTime resultTime) {
            this.resultTime = resultTime;
            return this;
        }

        public Exam build() {
            return new Exam(id, fachId, title, professorFachId, startTime, endTime, resultTime);
        }
    }
}
