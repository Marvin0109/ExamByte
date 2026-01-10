package exambyte.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class QuestionData {

    @NotBlank(message = "Fragetext darf nicht leer sein!")
    private String questionText;

    private String type;

    @Min(value = 1, message = "Punkte müssen mehr als 0 sein")
    private int punkte;

    private String choices;
    private String correctAnswer;
    private String correctAnswers;

    private UUID fachId;

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

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public void setFachId(UUID fachId) {
        this.fachId = fachId;
    }

    public UUID getFachId() {
        return fachId;
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
