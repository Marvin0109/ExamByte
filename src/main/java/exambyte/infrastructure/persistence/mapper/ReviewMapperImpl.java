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
                .id(null)
                .fachId(entity.getFachId())
                .antwortFachId(entity.getAntwortFachId())
                .korrektorFachId(entity.getKorrektorFachId())
                .bewertung(entity.getBewertung())
                .punkte(entity.getPunkte())
                .build();
    }

    @Override
    public ReviewEntity toEntity(Review review) {
        return new ReviewEntity.ReviewEntityBuilder()
                .id(null)
                .fachId(review.getFachId())
                .antwortFachId(review.getAntwortFachId())
                .korrektorFachId(review.getKorrektorFachId())
                .bewertung(review.getBewertung())
                .punkte(review.getPunkte())
                .build();
    }
}
