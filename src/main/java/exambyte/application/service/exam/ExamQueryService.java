package exambyte.application.service.exam;

import exambyte.application.dto.ExamDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExamQueryService {
    ExamDTO getExam(UUID examId);

    UUID getExamIdByStartTime(LocalDateTime start);

    List<ExamDTO> getAllExams();

    boolean hasStudentSubmittedExam(UUID examId, String studentName);

    void deleteByFachId(UUID examId);

    void resetAllExamDataCascade();
}
