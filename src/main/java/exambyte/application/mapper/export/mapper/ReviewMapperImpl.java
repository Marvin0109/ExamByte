package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.exam.Review;
import exambyte.infrastructure.entity.ReviewEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toDomain(ReviewEntity entity) {

        return new Review.ReviewBuilder()
                .id(entity.getId())
                .answerId(entity.getAnswerId())
                .reviewerId(entity.getReviewerId())
                .text(entity.getText())
                .points(entity.getPoints() / 2.0)
                .build();
    }

    @Override
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
