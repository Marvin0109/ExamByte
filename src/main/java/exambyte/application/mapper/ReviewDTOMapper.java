package exambyte.application.mapper;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.model.exam.Review;

import java.util.List;

public interface ReviewDTOMapper {

    ReviewDTO toDTO(Review review);

    List<ReviewDTO> toReviewDTOList(List<Review> reviews);

    Review toDomain(ReviewDTO dto);
}
