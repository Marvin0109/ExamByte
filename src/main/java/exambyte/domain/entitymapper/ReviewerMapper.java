package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.infrastructure.persistence.entities.ReviewerEntity;

public interface ReviewerMapper {

    Reviewer toDomain(ReviewerEntity reviewerEntity);

    public ReviewerEntity toEntity(Reviewer reviewer);
}
