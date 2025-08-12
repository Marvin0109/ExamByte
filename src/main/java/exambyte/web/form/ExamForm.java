package exambyte.web.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExamForm {

    @NotBlank(message = "Titel darf nicht leer sein!")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Startzeit muss gesetzt werden!")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Frist muss gesetzt werden!")
    private LocalDateTime end;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Ergebniszeit muss gesetzt werden!")
    private LocalDateTime result;

    @Valid
    private List<QuestionData> questions = new ArrayList<>();

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
        this.questions = questions;
    }

    // Validierungsmethode
    public boolean isValid() {
        return start != null && end != null && result != null && start.isBefore(end) && end.isBefore(result);
    }
}
