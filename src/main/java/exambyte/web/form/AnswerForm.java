package exambyte.web.form;

import java.util.UUID;

public class AnswerForm {

    private String frageText;
    private int maxPunkte;
    private String antwort;
    private UUID antwortFachId;

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

    public UUID getAntwortFachId() {
        return antwortFachId;
    }

    public void setAntwortFachId(UUID antwortFachId) {
        this.antwortFachId = antwortFachId;
    }
}
