package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Review;
import exambyte.persistence.entities.ReviewEntity;

public class ReviewMapper {

    public Review toDomain(ReviewEntity entity) {
        return Review.of(
                null,
                entity.getFachId(),
                entity.getAntwortFachId(),
                entity.getKorrektorFachId(),
                entity.getBewertung(),
                entity.getPunkte()
        );
    }

    public ReviewEntity toEntity(Review review) {
        return new ReviewEntity(
                null,
                review.getFachId(),
                review.getAntwortFachId(),
                review.getKorrektorId(),
                review.getBewertung(),
                review.getPunkte()
        );
    }
}
