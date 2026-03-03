package exambyte.web.form.create_review;

import java.util.UUID;

public class AnswerForm {

    private String frageText;
    private int maxPunkte;
    private String antwort;
    private UUID antwortId;

    public  String getFrageText() {
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
