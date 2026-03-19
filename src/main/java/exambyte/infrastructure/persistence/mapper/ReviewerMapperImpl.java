package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.entitymapper.ReviewerMapper;
import exambyte.infrastructure.persistence.entities.ReviewerEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewerMapperImpl implements ReviewerMapper {

    @Override
    public Reviewer toDomain(ReviewerEntity entity) {
        return new Reviewer.ReviewerBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public ReviewerEntity toEntity(Reviewer reviewer) {
        return new ReviewerEntity.ReviewerEntityBuilder()
                .id(reviewer.id())
                .name(reviewer.getName())
                .build();
    }
}
