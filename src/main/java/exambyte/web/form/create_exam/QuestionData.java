package exambyte.web.form.create_exam;

import exambyte.web.form.validation.HalfPoints;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class QuestionData {

    @NotBlank(message = "Fragetext darf nicht leer sein!")
    private String questionText;

    private String type;

    @NotNull(message = "Punkte müssen angegeben werden")
    @HalfPoints
    @DecimalMin(value = "0.5", message = "Punkte müssen mehr als 0.5 sein")
    private Double punkte;

    private String choices;
    private String correctAnswer;
    private String correctAnswers;

    private UUID id;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPunkte() {
        return punkte;
    }

    public void setPunkte(Double punkte) {
        this.punkte = punkte;
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
