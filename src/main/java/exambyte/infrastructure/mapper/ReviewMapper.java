package exambyte.infrastructure.mapper;

import exambyte.domain.model.exam.Review;
import exambyte.infrastructure.entity.ReviewEntity;

public interface ReviewMapper {

    Review toDomain(ReviewEntity entity);

    ReviewEntity toEntity(Review review);
}
