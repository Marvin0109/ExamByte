package exambyte.web.form.create_review;

import java.util.UUID;

public class AnswerForm {

    private String frageText;
    private double maxPunkte;
    private String antwort;
    private UUID antwortId;

    public  String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public double getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(double maxPunkte) {
        this.maxPunkte = maxPunkte;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }

    public UUID getAntwortId() {
        return antwortId;
    }

    public void setAntwortId(UUID antwortId) {
        this.antwortId = antwortId;
    }
}
