package exambyte.application.service.usecase;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.service.query.ReviewQueryService;
import exambyte.application.service.query.AnswerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewManagementServiceImpl implements ReviewManagementService {

    private final ReviewQueryService reviewQueryService;
    private final AnswerService answerService;

    public ReviewManagementServiceImpl(ReviewQueryService reviewQueryService,
                                       AnswerService answerService) {
        this.reviewQueryService = reviewQueryService;
        this.answerService = answerService;
    }

    @Override
    public double getReviewCoverage(UUID examId) {
        List<AnswerDTO> answers = answerService.getFreeResponseAnswersForExam(examId);

        List<ReviewDTO> reviewsTotal = new ArrayList<>();

        for (AnswerDTO answerDTO : answers) {
            ReviewDTO reviewDTO = reviewQueryService.getReviewByAnswerId(answerDTO.id());
            if (reviewDTO != null) {
                reviewsTotal.add(reviewDTO);
            }
        }

        double coverage = answers.isEmpty()
                ? 0.0
                : (double) reviewsTotal.size() / answers.size() * 100;

        return Math.round(coverage * 100.0) / 100.0;
    }

    @Override
    public boolean submitHasReview(UUID examId, UUID studentId) {
        List<AnswerDTO> answers = answerService.getFreeResponseAnswersForExam(examId);

        List<UUID> studentAnswerList = answers.stream()
                .filter(a -> a.studentId().equals(studentId))
                .map(AnswerDTO::id)
                .toList();

        for (UUID id : studentAnswerList) {
            if (reviewQueryService.getReviewByAnswerId(id) == null) {
                return false;
            }
        }

        return true;
    }
}
