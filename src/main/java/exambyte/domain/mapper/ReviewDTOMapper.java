package exambyte.domain.mapper;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.aggregate.exam.Review;

import java.util.List;

public interface ReviewDTOMapper {

    ReviewDTO toDTO(Review review);

    List<ReviewDTO> toReviewDTOList(List<Review> reviews);
}
