package exambyte.application.service.submission;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.application.dto.VersuchDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExamSubmissionService {
    String createExam(String profName, String title, LocalDateTime start, LocalDateTime end, LocalDateTime result);

    boolean submitExam(String studentName, Map<String, List<String>> antworten, UUID examId);

    void removeOldAnswers(UUID examId, String name);

    VersuchDTO getSubmission(UUID examFachId, String studentName);

    List<StudentDTO> getStudentSubmittedExam(UUID examId);

    List<AntwortDTO> getFreitextAntwortenForExam(UUID examId);
}
