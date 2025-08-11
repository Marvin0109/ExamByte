package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.ExamService;
import exambyte.infrastructure.NichtVorhandenException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public List<Exam> alleExams() {
        return examRepository.findAll().stream().toList();

    }

    @Override
    public Exam getExam(UUID fachId) {
        return examRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void addExam(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    public void deleteAll() {
        examRepository.deleteAll();
    }

    // TODO: deleteByFachId()
}
