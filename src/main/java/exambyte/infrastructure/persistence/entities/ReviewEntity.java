package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("review")
public class ReviewEntity {

    @Id
    private UUID id;

    @Column("answer_id")
    private final UUID answerId;

    @Column("reviewer_id")
    private final UUID reviewerId;

    @Column("text")
    private String text;

    @Column("points")
    private int points;

    private ReviewEntity(UUID id, UUID answerId, UUID reviewerId, String text, int points) {
        this.id = id;
        this.text = text;
        this.points = points;
        this.answerId = answerId;
        this.reviewerId = reviewerId;
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

    public void setText(String text) {
        this.text = text;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public static class ReviewEntityBuilder {
        private UUID id;
        private String text;
        private int points;
        private UUID answerId;
        private UUID reviewerId;

        public ReviewEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReviewEntityBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ReviewEntityBuilder points(int points) {
            this.points = points;
            return this;
        }

        public ReviewEntityBuilder answerId(UUID answerId) {
            this.answerId = answerId;
            return this;
        }

        public ReviewEntityBuilder reviewerId(UUID reviewerId) {
            this.reviewerId = reviewerId;
            return this;
        }

        public ReviewEntity build() {
            checkID(answerId, "Antwort-ID fehlt");
            checkID(reviewerId, "Reviewer-ID fehlt");
            if (text == null || text.isBlank()) {
                throw new IllegalStateException("Bewertung fehlt");
            }
            if (points < 0) {
                throw new IllegalStateException("Punkte dürfen nicht negativ sein");
            }
            return new ReviewEntity(id, answerId, reviewerId, text, points);
        }

        private static void checkID(UUID id, String message) {
            if (id == null) {
                throw new IllegalStateException(message);
            }
        }
    }
}
