package exambyte.application.service;

import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;

import java.util.List;

public interface CsvExportService {

    byte[] exportExamToCsv(List<ExamExportDTO> examExportDTO) throws Exception;
    byte[] exportReviewToCsv(List<ReviewExportDTO> reviewExportDTO) throws Exception;
}
