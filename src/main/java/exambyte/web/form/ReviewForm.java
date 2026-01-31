package exambyte.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ReviewForm {

    @NotBlank(message = "Ein Bewertungstext muss vorhanden sein")
    private String bewertung;

    @Min(value = 0, message = "Punkte dürfen nicht negativ sein")
    private int punkteVergeben;

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public int getPunkteVergeben() {
        return punkteVergeben;
    }

    public void setPunkteVergeben(int punkteVergeben) {
        this.punkteVergeben = punkteVergeben;
    }
}
