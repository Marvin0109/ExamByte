package exambyte.application.dto.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"Pruefungstitel", "Autor", "Gesamtpunktzahl", "Aufgabenstellung", "Aufgabentyp",
        "Punkte_fuer_Aufgabe", "Antwortoptionen", "Loesungen"})
public class ExamExportDTO {

    @JsonProperty("Pruefungstitel")
    private String examTitle;

    @JsonProperty("Autor")
    private String author;

    @JsonProperty("Aufgabenstellung")
    private String questionText;

    @JsonProperty("Gesamtpunktzahl")
    private double totalPoints;

    @JsonProperty("Aufgabentyp")
    private String questionType;

    @JsonProperty("Punkte_fuer_Aufgabe")
    private double questionPoints;

    @JsonProperty("Antwortoptionen")
    private String choices;

    @JsonProperty("Loesungen")
    private String solution;

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public double getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(double questionPoints) {
        this.questionPoints = questionPoints;
    }
}
