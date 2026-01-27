package exambyte.web.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExamForm {

    @NotBlank(message = "Titel darf nicht leer sein!")
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Startzeit muss gesetzt werden!")
    private LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Frist muss gesetzt werden!")
    private LocalDateTime end;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Ergebniszeit muss gesetzt werden!")
    private LocalDateTime result;

    @NotEmpty
    private List<@Valid QuestionData> questions = new ArrayList<>();

    private UUID fachId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getResult() {
        return result;
    }

    public void setResult(LocalDateTime result) {
        this.result = result;
    }

    public List<QuestionData> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionData> questions) {
    this.questions = new ArrayList<>(questions);
    }

    public UUID getFachId() {
        return fachId;
    }

    public void setFachId(UUID fachId) {
        this.fachId = fachId;
    }
}
