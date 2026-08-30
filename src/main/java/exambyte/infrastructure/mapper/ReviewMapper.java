package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Review;
import exambyte.infrastructure.entity.ReviewEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toDomain(ReviewEntity entity) {

        return new Review.ReviewBuilder()
                .id(entity.getId())
                .answerId(entity.getAnswerId())
                .reviewerId(entity.getReviewerId())
                .text(entity.getText())
                .points(entity.getPoints() / 2.0)
                .build();
    }

    public ReviewEntity toEntity(Review review) {
        int pointsForDb = (int) Math.round(review.getPoints() * 2.0);

        return new ReviewEntity.ReviewEntityBuilder()
                .id(review.getId())
                .answerId(review.getAnswerId())
                .reviewerId(review.getReviewerId())
                .text(review.getText())
                .points(pointsForDb)
                .build();
    }
}
