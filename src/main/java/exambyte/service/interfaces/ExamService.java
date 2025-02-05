package exambyte.service.interfaces;

import exambyte.domain.aggregate.exam.Exam;

import java.util.List;
import java.util.UUID;

public interface ExamService {

    List<Exam> alleExams();

    Exam getExam(UUID fachId);

    void addExam(Exam exam);
}
