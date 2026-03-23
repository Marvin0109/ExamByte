package exambyte.application.service.export;

import exambyte.application.dto.export.ExamExportDTO;

import java.util.List;
import java.util.UUID;

public interface ExamExportService {

    List<ExamExportDTO> createExamExport(UUID examId);
}
