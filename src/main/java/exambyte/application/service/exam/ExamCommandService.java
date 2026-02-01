package exambyte.application.service.exam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExamCommandService {
    String createExam(String profName, String title, LocalDateTime start, LocalDateTime end, LocalDateTime result);

    boolean submitExam(String studentName, Map<String, List<String>> antworten, UUID examId);
}
