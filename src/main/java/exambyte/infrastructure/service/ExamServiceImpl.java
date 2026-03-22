package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.ExamService;
import exambyte.infrastructure.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository repository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.repository = examRepository;
    }

    @Override
    public List<Exam> allExams() {
        return repository.findAll().stream().toList();
    }

    @Override
    public Exam getExam(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void addExam(Exam exam) {
        repository.save(exam);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
