package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.user.Reviewer;
import exambyte.infrastructure.entity.ReviewerEntity;
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
