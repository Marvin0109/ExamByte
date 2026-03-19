package exambyte.infrastructure.mapper;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.mapper.ReviewDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewDTOMapperImpl implements ReviewDTOMapper {

    @Override
    public ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getAntwortId(),
                review.getReviewerId(),
                review.getBewertung(),
                review.getPunkte());
    }

    @Override
    public List<ReviewDTO> toReviewDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Review toDomain(ReviewDTO dto) {
        return new Review.ReviewBuilder()
                .id(dto.id())
                .antwortId(dto.antwortId())
                .reviewerId(dto.reviewerId())
                .bewertung(dto.bewertung())
                .punkte(dto.punkte())
                .build();
    }
}
