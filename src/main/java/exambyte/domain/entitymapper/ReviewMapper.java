package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.Review;
import exambyte.infrastructure.persistence.entities.ReviewEntity;

public interface ReviewMapper {

    Review toDomain(ReviewEntity reviewEntity);

    ReviewEntity toEntity(Review review);
}
