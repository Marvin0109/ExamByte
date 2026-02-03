package exambyte.application.service.usecase;

import java.util.UUID;

public interface ReviewManagementService {

    double getReviewCoverage(UUID examId);

    boolean submitHasReview(UUID examId, UUID studentId);
}
