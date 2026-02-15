package exambyte.web.service.csv_dto;

public class ReviewExportDTO {

    private String examTitle;
    private String author;
    private String frageText;
    private int maxPunkte;
    private String frageTyp;
    private String antwortMoeglichkeiten;
    private String studiAntworten;
    private String bewertung;
    private double erreichtePunkte;

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

    public String getStudiAntworten() {
        return studiAntworten;
    }

    public void setStudiAntworten(String studiAntworten) {
        this.studiAntworten = studiAntworten;
    }

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public double getErreichtePunkte() {
        return erreichtePunkte;
    }

    public void setErreichtePunkte(double erreichtePunkte) {
        this.erreichtePunkte = erreichtePunkte;
    }
}
