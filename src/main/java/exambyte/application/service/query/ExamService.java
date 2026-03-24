package exambyte.application.service.query;

import exambyte.application.dto.ExamDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExamService {
    ExamDTO getExam(UUID examId);

    UUID getExamIdByStartTime(LocalDateTime start);

    List<ExamDTO> getAllExams();

    boolean hasStudentSubmittedExam(UUID examId, String studentName);

    void deleteById(UUID examId);

    void deleteAll();

    void resetAllExamDataCascade();

    void addExam(ExamDTO examDTO);
}
