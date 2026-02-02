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
    public UUID getReviewIdByAntwortId(UUID antwortId) {
        return reviewService.getReviewByAntwortFachId(antwortId).getAntwortFachId();
    }

    @Override
    public boolean antwortHasReview(UUID antwortId) {
        return reviewService.getReviewByAntwortFachId(antwortId) != null;
    }

    @Override
    public void createReview(String bewertung, int punkte, UUID antwortId, UUID korrektorId) {
        ReviewDTO review = new ReviewDTO(
                null,
                antwortId,
                korrektorId,
                bewertung,
                punkte);

        reviewService.addReview(reviewDTOMapper.toDomain(review));
    }

    @Override
    public ReviewDTO getReviewByAntwortId(UUID antwortId) {
        Review review = reviewService.getReviewByAntwortFachId(antwortId);
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
