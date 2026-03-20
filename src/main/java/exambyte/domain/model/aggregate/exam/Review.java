package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class Review {

    private final UUID id;
    private final UUID answerId;
    private final UUID reviewerId;
    private final String bewertung;
    private final double punkte;

    private Review(UUID id, UUID answerId, UUID reviewerId, String bewertung, double punkte) {
        this.id = id;
        this.answerId = answerId;
        this.reviewerId = reviewerId;
        this.bewertung = bewertung;
        this.punkte = punkte;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAnswerId() {
        return answerId;
    }

    public UUID getReviewerId() {
        return reviewerId;
    }

    public String getBewertung() {
        return bewertung;
    }

    public double getPunkte() {
        return punkte;
    }

    public static class ReviewBuilder {
        private UUID id;
        private UUID answerId;
        private UUID reviewerId;
        private String bewertung;
        private double punkte;

        public ReviewBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReviewBuilder answerId(UUID answerId) {
            this.answerId = answerId;
            return this;
        }

        public ReviewBuilder reviewerId(UUID reviewerId) {
            this.reviewerId = reviewerId;
            return this;
        }

        public ReviewBuilder bewertung(String bewertung) {
            this.bewertung = bewertung;
            return this;
        }

        public ReviewBuilder punkte(double punkte) {
            this.punkte = punkte;
            return this;
        }

        public Review build() {
            return new Review(id, answerId, reviewerId, bewertung, punkte);
        }
    }
}
