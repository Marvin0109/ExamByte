package exambyte.service;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        LocalDateTime startTime = exam.getStartTime();
        LocalDateTime endTime = exam.getEndTime();
        LocalDateTime resultTime = exam.getResultTime();
        if (startTime.isAfter(endTime) || endTime.isAfter(resultTime) || startTime.isAfter(resultTime)) {
            throw new NichtVorhandenException();
        }
        examRepository.save(exam);
        return exam.getFachId();
    }
}
