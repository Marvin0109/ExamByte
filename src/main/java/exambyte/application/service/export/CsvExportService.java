package exambyte.application.service.export;

import exambyte.application.dto.export.ExamExportDTO;
import exambyte.application.dto.export.ReviewExportDTO;

import java.util.List;

public interface CsvExportService {

    byte[] exportExamToCsv(List<ExamExportDTO> examExportDTO);
    byte[] exportReviewToCsv(List<ReviewExportDTO> reviewExportDTO);
}
