package exambyte.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ExamForm {

    @NotBlank(message = "Titel darf nicht leer sein!")
    private String title;

    @NotNull(message = "Startzeit muss gesetzt werden!")
    private LocalDateTime start;

    @NotNull(message = "Frist muss gesetzt werden!")
    private LocalDateTime end;

    @NotNull(message = "Ergebniszeit muss gesetzt werden!")
    private LocalDateTime result;

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
}
