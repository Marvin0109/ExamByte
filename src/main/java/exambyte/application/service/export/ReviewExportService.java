package exambyte.application.service.export;

import exambyte.application.dto.export.ReviewExportDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewExportService {

    List<ReviewExportDTO> createReviewExport(UUID examId, String studentName);
}
