package exambyte.application.dto.csv_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"Pruefungstitel", "Autor", "Gesamtpunktzahl", "Aufgabenstellung", "Aufgabentyp",
        "Punkte_fuer_Aufgabe", "Studenten_Antwort", "Bewertungstext", "Punkte_vergeben"})
public class ReviewExportDTO {

    @JsonProperty("Pruefungstitel")
    private String examTitle;

    @JsonProperty("Autor")
    private String author;

    @JsonProperty("Aufgabenstellung")
    private String questionText;

    @JsonProperty("Gesamtpunktzahl")
    private double totalPoints;

    @JsonProperty("Punkte_fuer_Aufgabe")
    private double questionPoints;

    @JsonProperty("Aufgabentyp")
    private String questionType;

    @JsonProperty("Studenten_Antwort")
    private String studentAnswer;

    @JsonProperty("Bewertungstext")
    private String reviewText;

    @JsonProperty("Punkte_vergeben")
    private double reviewPoints;

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

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public double getReviewPoints() {
        return reviewPoints;
    }

    public void setReviewPoints(double reviewPoints) {
        this.reviewPoints = reviewPoints;
    }

    public double getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(double questionPoints) {
        this.questionPoints = questionPoints;
    }
}
