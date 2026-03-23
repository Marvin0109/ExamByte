package exambyte.domain.service;

import exambyte.domain.model.exam.Exam;

import java.util.List;
import java.util.UUID;

public interface ExamService {

    List<Exam> allExams();

    Exam getExam(UUID id);

    void addExam(Exam exam);

    void deleteById(UUID id);

    void deleteAll();
}
