package exambyte.application.service.query;

import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewService reviewService;
    private final ReviewDTOMapper reviewDTOMapper;

    public ReviewQueryServiceImpl(ReviewService reviewService, ReviewDTOMapper reviewDTOMapper) {
        this.reviewService = reviewService;
        this.reviewDTOMapper = reviewDTOMapper;
    }

    @Override
    public UUID getReviewIdByAnswerId(UUID answerId) {
        return reviewService.getReviewByAnswerId(answerId).getAnswerId();
    }

    @Override
    public boolean answerHasReview(UUID answerId) {
        return reviewService.getReviewByAnswerId(answerId) != null;
    }

    @Override
    public void createReview(String bewertung, double punkte, UUID answerId, UUID reviewerId) {
        Review loaded = reviewService.getReviewByAnswerId(answerId);

        UUID reviewId = loaded != null ? loaded.getId() : null;

        Review review = reviewDTOMapper.toDomain(
                new ReviewDTO(
                        reviewId,
                        answerId,
                        reviewerId,
                        bewertung,
                        punkte)
        );

        reviewService.addReview(review);
    }

    @Override
    public ReviewDTO getReviewByAnswerId(UUID answerId) {
        Review review = reviewService.getReviewByAnswerId(answerId);
        if (review != null) {
            return reviewDTOMapper.toDTO(review);
        }
        return null;
    }

    @Override
    public void deleteReview(UUID id) {
        reviewService.deleteReview(id);
    }
}
