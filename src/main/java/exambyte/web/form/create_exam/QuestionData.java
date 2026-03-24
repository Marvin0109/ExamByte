package exambyte.web.form.create_exam;

import exambyte.web.form.validation.HalfPoints;
import exambyte.web.form.validation.ValidQuestion;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@ValidQuestion
public class QuestionData {

    @NotBlank(message = "Fragetext darf nicht leer sein!")
    private String text;

    private String type;

    @NotNull(message = "Punkte müssen angegeben werden")
    @HalfPoints
    @DecimalMin(value = "0.5", message = "Punkte müssen mehr als 0.5 sein")
    private Double points;

    private String choices;
    private String correctAnswer;
    private String correctAnswers;

    private UUID id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getChoices() {
        return choices != null ? choices : "";
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer != null ? correctAnswer : "";
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswers() {
        return correctAnswers != null ? correctAnswers : "";
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
