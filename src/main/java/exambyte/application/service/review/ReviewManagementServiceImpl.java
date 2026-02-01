package exambyte.application.service.review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.submission.AnswerSubmissionService;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.service.KorrektorService;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewManagementServiceImpl implements ReviewManagementService {

    private final KorrektorService korrektorService;
    private final ReviewService reviewService;
    private final ReviewDTOMapper reviewDTOMapper;

    private final AnswerSubmissionService answerSubmissionService;


    public ReviewManagementServiceImpl(KorrektorService korrektorService,
                                       ReviewService reviewService,
                                       AnswerSubmissionService answerSubmissionService,
                                       ReviewDTOMapper reviewDTOMapper) {
        this.korrektorService = korrektorService;
        this.reviewService = reviewService;
        this.answerSubmissionService = answerSubmissionService;
        this.reviewDTOMapper = reviewDTOMapper;
    }

    @Override
    public void saveAutomaticReviewer() {
        if (korrektorService.getKorrektorByName("Automatischer Korrektor").isEmpty()) {
            korrektorService.saveKorrektor("Automatischer Korrektor");
        }
    }

    @Override
    public double getReviewCoverage(UUID examFachId) {
        List<AntwortDTO> antworten = answerSubmissionService.getFreitextAntwortenForExam(examFachId);

        List<ReviewDTO> reviewsTotal = new ArrayList<>();

        for (AntwortDTO antwortDTO : antworten) {
            Review review = reviewService.getReviewByAntwortFachId(antwortDTO.fachId());
            if (review != null) {
                reviewsTotal.add(reviewDTOMapper.toDTO(review));
            }
        }

        double coverage = antworten.isEmpty()
                ? 0.0
                : (double) reviewsTotal.size() / antworten.size() * 100;

        return Math.round(coverage * 100.0) / 100.0;
    }

    @Override
    public boolean submitHasReview(UUID examFachId, UUID studentId) {
        List<AntwortDTO> antworten = answerSubmissionService.getFreitextAntwortenForExam(examFachId);

        List<UUID> studentAntwortList = antworten.stream()
                .filter(a -> a.studentFachId().equals(studentId))
                .map(AntwortDTO::fachId)
                .toList();

        for (UUID uuid : studentAntwortList) {
            if (reviewService.getReviewByAntwortFachId(uuid) == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean antwortHasReview(UUID antwortId) {
        return reviewService.getReviewByAntwortFachId(antwortId) != null;
    }

    @Override
    public void createReview(String bewertung, int punkte, UUID antwortId, UUID korrektorId) {
        Review review = new Review.ReviewBuilder()
                .korrektorFachId(korrektorId)
                .punkte(punkte)
                .antwortFachId(antwortId)
                .bewertung(bewertung)
                .build();

        reviewService.addReview(review);
    }

    @Override
    public UUID getReviewerIdByName(String name) {
        Optional<Korrektor> k = korrektorService.getKorrektorByName(name);
        return k.map(Korrektor::uuid).orElse(null);
    }
}
