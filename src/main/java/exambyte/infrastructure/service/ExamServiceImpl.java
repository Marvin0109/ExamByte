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
    public void addExam(Long id, UUID fachId, String title, UUID profFachId, LocalDateTime startTime,
                        LocalDateTime endTime, LocalDateTime resultTime) {
        Exam exam = new Exam.ExamBuilder()
                .id(id)
                .fachId(fachId)
                .title(title)
                .professorFachId(profFachId)
                .startTime(startTime)
                .endTime(endTime).resultTime(resultTime).build();
        examRepository.save(exam);
    }
}
