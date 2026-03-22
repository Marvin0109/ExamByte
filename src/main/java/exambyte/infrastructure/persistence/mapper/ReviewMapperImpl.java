package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.entitymapper.ReviewMapper;
import exambyte.infrastructure.persistence.entities.ReviewEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toDomain(ReviewEntity entity) {

        return new Review.ReviewBuilder()
                .id(entity.getId())
                .answerId(entity.getAnswerId())
                .reviewerId(entity.getReviewerId())
                .bewertung(entity.getText())
                .punkte(entity.getPoints() / 2.0)
                .build();
    }

    @Override
    public ReviewEntity toEntity(Review review) {
        int punkte = (int) Math.round(review.getPunkte() * 2.0);

        return new ReviewEntity.ReviewEntityBuilder()
                .id(review.getId())
                .answerId(review.getAnswerId())
                .reviewerId(review.getReviewerId())
                .text(review.getBewertung())
                .points(punkte)
                .build();
    }
}
