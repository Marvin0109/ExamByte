package exambyte.domain.model.aggregate.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Exam {

    private final UUID id;
    private final String title;
    private final UUID professorId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final LocalDateTime resultTime;

    private Exam(UUID id, String title, UUID professorId,
                 LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
        this.id = id;
        this.title = title;
        this.professorId = professorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
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
        private UUID id;
        private String title;
        private UUID professorId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private LocalDateTime resultTime;

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
            return new Exam(id, title, professorId, startTime, endTime, resultTime);
        }
    }
}
