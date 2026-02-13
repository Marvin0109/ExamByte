package exambyte.application.service.usecase;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.VersuchDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExamManagementService {
    String createExam(String profName, String title, LocalDateTime start, LocalDateTime end, LocalDateTime result);

    SubmitExamResult submitExam(String studentName, Map<String, List<String>> antworten, UUID examId);

    void removeOldAnswers(UUID examId, String name);

    VersuchDTO getSubmission(UUID examFachId, String studentName);

    List<ExamDTO> getAllExams();

    boolean hasStudentSubmittedExam(UUID examId, String studentName);

    ExamDTO getExam(UUID examId);

    UUID getExamIdByStartTime(LocalDateTime startTime);

    boolean deleteById(UUID examId);

    boolean resetAllExamDataCascade();

    boolean allowedToViewReview(UUID examId);
}
