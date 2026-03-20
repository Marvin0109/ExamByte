package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import exambyte.domain.service.AnswerService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository repository;

    public AnswerServiceImpl(AnswerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Answer findByFrageId(UUID frageId) {
        return repository.findByFrageId(frageId);
    }

    @Override
    public void addAnswer(Answer answer) {
        repository.save(answer);
    }

    @Override
    public Answer findByStudentAndFrage(UUID studentId, UUID examId) {
        return repository.findByStudentIdAndFrageId(studentId, examId)
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAnswer(UUID id) {
        repository.deleteAnswer(id);
    }
}
