package exambyte.web.form.create_review;

import exambyte.web.form.HalfPoints;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ReviewForm {

    @NotBlank(message = "Ein Bewertungstext muss vorhanden sein")
    private String bewertung;

    @HalfPoints
    @Min(value = 0, message = "Punkte dürfen nicht negativ sein")
    private double punkteVergeben;

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public double getPunkteVergeben() {
        return punkteVergeben;
    }

    public void setPunkteVergeben(double punkteVergeben) {
        this.punkteVergeben = punkteVergeben;
    }
}
