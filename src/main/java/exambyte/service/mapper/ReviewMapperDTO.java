package exambyte.service.mapper;

import exambyte.domain.aggregate.exam.Review;
import exambyte.service.dto.ReviewDTO;

public class ReviewMapperDTO {

    public static ReviewDTO toDTO(Review review) {
        return new ReviewDTO(null, review.getFachId(), review.getAntwortFachId(), review.getKorrektorFachId(),
                review.getBewertung(), review.getPunkte());
    }

    public static Review toEntity(ReviewDTO dto) {
        return new Review.ReviewBuilder()
                .id(null)
                .fachId(dto.getFachId())
                .antwortFachId(dto.getAntwortFachId())
                .korrektorFachId(dto.getKorrektorId())
                .bewertung(dto.getBewertung())
                .punkte(dto.getPunkte())
                .build();
    }
}
