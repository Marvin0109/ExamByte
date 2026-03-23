package exambyte.application.mapper;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.model.exam.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewDTOMapperImpl implements ReviewDTOMapper {

    @Override
    public ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getAnswerId(),
                review.getReviewerId(),
                review.getText(),
                review.getPoints());
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
                .answerId(dto.answerId())
                .reviewerId(dto.reviewerId())
                .text(dto.text())
                .points(dto.points())
                .build();
    }
}
