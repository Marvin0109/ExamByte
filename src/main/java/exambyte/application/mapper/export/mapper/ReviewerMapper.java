package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.user.Reviewer;
import exambyte.infrastructure.entity.ReviewerEntity;

public interface ReviewerMapper {

    Reviewer toDomain(ReviewerEntity entity);

    ReviewerEntity toEntity(Reviewer reviewer);
}
