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
                .id(entity.getId())
                .antwortId(entity.getAntwortId())
                .korrektorId(entity.getKorrektorId())
                .bewertung(entity.getBewertung())
                .punkte(entity.getPunkte() / 2.0)
                .build();
    }

    @Override
    public ReviewEntity toEntity(Review review) {
        int punkte = (int) Math.round(review.getPunkte() * 2.0);

        return new ReviewEntity.ReviewEntityBuilder()
                .id(review.getId())
                .antwortId(review.getAntwortId())
                .korrektorId(review.getKorrektorId())
                .bewertung(review.getBewertung())
                .punkte(punkte)
                .build();
    }
}
