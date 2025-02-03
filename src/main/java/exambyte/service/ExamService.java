package exambyte.service;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public List<Exam> alleExams() {
        return examRepository.findAll().stream().sorted().toList();
    }

    public Exam getExam(UUID fachId) {
        return examRepository.findByFachId(fachId).
                orElseThrow(NichtVorhandenException::new);
    }

    public UUID addExam(Exam exam) {
        examRepository.save(exam);
        return exam.getFachId();
    }
}
