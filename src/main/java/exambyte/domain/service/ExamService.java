package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Exam;

import java.util.List;
import java.util.UUID;

public interface ExamService {

    List<Exam> allExams();

    Exam getExam(UUID fachId);

    void addExam(Exam exam);

    void deleteByFachId(UUID fachId);

    void deleteAll();
}
