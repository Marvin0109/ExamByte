package exambyte.domain.service;

import exambyte.domain.aggregate.exam.Exam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExamService {

    List<Exam> alleExams();

    Exam getExam(UUID fachId);

    void addExam(Long id, UUID fachId, String title, UUID profFachId, LocalDateTime startTime,
                 LocalDateTime endTime, LocalDateTime resultTime);
}
