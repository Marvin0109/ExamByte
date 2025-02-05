package exambyte.service.interfaces;

import exambyte.domain.aggregate.exam.Exam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExamManagementService {

    boolean createExam(String profName, String title,
                       LocalDateTime startDate, LocalDateTime endDate, LocalDateTime resultTime);

    List<Exam> getAllExams();

    boolean isExamAlreadySubmitted(UUID examFachId, String studentName);

    boolean submitExam(String studentLogin, List<UUID> frageFachIds, List<String> antwortTexte);

    Exam getExam(UUID examFachId);
}
