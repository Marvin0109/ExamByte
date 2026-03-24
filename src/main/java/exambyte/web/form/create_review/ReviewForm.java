package exambyte.web.form.create_review;

import exambyte.web.form.validation.HalfPointsAboveZero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewForm {

    @NotBlank(message = "Ein Bewertungstext muss vorhanden sein")
    private String reviewText;

    @NotNull(message = "Punkte müssen angegeben werden")
    @HalfPointsAboveZero
    @Min(value = 0, message = "Punkte dürfen nicht negativ sein")
    private Double points;

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }
}
