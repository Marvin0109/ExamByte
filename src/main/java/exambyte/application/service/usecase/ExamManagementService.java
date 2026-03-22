package exambyte.application.service.usecase;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.AttemptDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExamManagementService {
    String createExam(String profName, String title, LocalDateTime start, LocalDateTime end, LocalDateTime result);

    SubmitExamResult submitExam(String studentName, Map<String, List<String>> answers, UUID examId);

    AttemptDTO getSubmission(UUID examId, String studentName);

    List<ExamDTO> getAllExams();

    boolean hasStudentSubmittedExam(UUID examId, String studentName);

    ExamDTO getExam(UUID examId);

    UUID getExamIdByStartTime(LocalDateTime start);

    boolean deleteById(UUID examId);

    boolean resetAllExamDataCascade();

    boolean allowedToViewReview(UUID examId);
}
