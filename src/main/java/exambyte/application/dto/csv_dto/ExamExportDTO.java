package exambyte.application.dto.csv_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"examTitle", "author", "maxPunkte", "frageText", "frageTyp",
        "punkte", "antwortMoeglichkeiten", "loesungen"})
public class ExamExportDTO {

    private String examTitle;
    private String author;
    private String frageText;
    private int maxPunkte;
    private String frageTyp;
    private int punkte;
    private String antwortMoeglichkeiten;
    private String loesungen;

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

    public String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }

    public String getFrageTyp() {
        return frageTyp;
    }

    public void setFrageTyp(String frageTyp) {
        this.frageTyp = frageTyp;
    }

    public String getAntwortMoeglichkeiten() {
        return antwortMoeglichkeiten;
    }

    public void setAntwortMoeglichkeiten(String antwortMoeglichkeiten) {
        this.antwortMoeglichkeiten = antwortMoeglichkeiten;
    }

    public String getLoesungen() {
        return loesungen;
    }

    public void setLoesungen(String loesungen) {
        this.loesungen = loesungen;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }
}
