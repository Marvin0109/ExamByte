package exambyte.domain.aggregate.exam;

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
        this.fachId = fachId;
        this.id = id;
        this.title = title;
        this.professorFachId = professorFachId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
    }

    public static Exam of(Long id, UUID fachId, String title, UUID professorFachId,
                          LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
        return new Exam(id, fachId != null ? fachId : UUID.randomUUID()
                , title, professorFachId, startTime, endTime, resultTime);
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
}
