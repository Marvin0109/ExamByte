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

    @Column("bewertung")
    private String bewertung;

    @Column("punkte")
    private int punkte;

    private ReviewEntity(UUID id, UUID answerId, UUID reviewerId, String bewertung, int punkte) {
        this.id = id;
        this.bewertung = bewertung;
        this.punkte = punkte;
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

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public static class ReviewEntityBuilder {
        private UUID id;
        private String bewertung;
        private int punkte;
        private UUID answerId;
        private UUID reviewerId;

        public ReviewEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReviewEntityBuilder bewertung(String bewertung) {
            this.bewertung = bewertung;
            return this;
        }

        public ReviewEntityBuilder punkte(int punkte) {
            this.punkte = punkte;
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
            if (bewertung == null || bewertung.isBlank()) {
                throw new IllegalStateException("Bewertung fehlt");
            }
            if (punkte < 0) {
                throw new IllegalStateException("Punkte dürfen nicht negativ sein");
            }
            return new ReviewEntity(id, answerId, reviewerId, bewertung, punkte);
        }

        private static void checkID(UUID id, String message) {
            if (id == null) {
                throw new IllegalStateException(message);
            }
        }
    }
}
