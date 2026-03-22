package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class Review {

    private final UUID id;
    private final UUID answerId;
    private final UUID reviewerId;
    private final String text;
    private final double points;

    private Review(UUID id, UUID answerId, UUID reviewerId, String text, double points) {
        this.id = id;
        this.answerId = answerId;
        this.reviewerId = reviewerId;
        this.text = text;
        this.points = points;
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

    public String getText() {
        return text;
    }

    public double getPoints() {
        return points;
    }

    public static class ReviewBuilder {
        private UUID id;
        private UUID answerId;
        private UUID reviewerId;
        private String text;
        private double points;

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

        public ReviewBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ReviewBuilder points(double points) {
            this.points = points;
            return this;
        }

        public Review build() {
            return new Review(id, answerId, reviewerId, text, points);
        }
    }
}
