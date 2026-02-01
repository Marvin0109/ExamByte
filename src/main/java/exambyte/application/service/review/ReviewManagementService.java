package exambyte.application.service.review;

import java.util.UUID;

public interface ReviewManagementService {

    void saveAutomaticReviewer();

    double getReviewCoverage(UUID examId);

    boolean submitHasReview(UUID examId, UUID studentId);

    boolean antwortHasReview(UUID antwortId);

    void createReview(String bewertung, int punkte, UUID antwortId, UUID korrektorId);

    UUID getReviewerIdByName(String name);

    UUID getReviewIdByAntwortId(UUID antwortId);

    void deleteReview(UUID id);
}
