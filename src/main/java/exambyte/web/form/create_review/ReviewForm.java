package exambyte.web.form.create_review;

import exambyte.web.form.validation.HalfPointsAboveZero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewForm {

    @NotBlank(message = "Ein Bewertungstext muss vorhanden sein")
    private String bewertung;

    @NotNull(message = "Punkte müssen angegeben werden")
    @HalfPointsAboveZero
    @Min(value = 0, message = "Punkte dürfen nicht negativ sein")
    private Double punkteVergeben;

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public Double getPunkteVergeben() {
        return punkteVergeben;
    }

    public void setPunkteVergeben(Double punkteVergeben) {
        this.punkteVergeben = punkteVergeben;
    }
}
