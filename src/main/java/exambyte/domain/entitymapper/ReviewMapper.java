package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.exam.Review;
import exambyte.persistence.entities.ReviewEntity;

public interface ReviewMapper {

    Review toDomain(ReviewEntity reviewEntity);

    ReviewEntity toEntity(Review review);
}
