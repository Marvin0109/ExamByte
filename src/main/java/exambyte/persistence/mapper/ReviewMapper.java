package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Review;
import exambyte.persistence.entities.ReviewEntity;

public class ReviewMapper {

    public static Review toDomain(ReviewEntity entity) {
        return new Review.ReviewBuilder()
                .id(null)
                .fachId(entity.getFachId())
                .antwortFachId(entity.getAntwortFachId())
                .korrektorFachId(entity.getKorrektorFachId())
                .bewertung(entity.getBewertung())
                .punkte(entity.getPunkte())
                .build();
    }

    public ReviewEntity toEntity(Review review) {
        return new ReviewEntity.ReviewEntityBuilder()
                .id(null)
                .fachId(review.getFachId())
                .antwortFachId(review.getAntwortFachId())
                .korrektorFachId(review.getKorrektorFachId())
                .bewertung(review.getBewertung())
                .punkte(review.getPunkte())
                .build();
    }
}
