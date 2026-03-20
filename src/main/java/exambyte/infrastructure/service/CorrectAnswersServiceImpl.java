package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.domain.repository.CorrectAnswersRepository;
import exambyte.domain.service.CorrectAnswersService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CorrectAnswersServiceImpl implements CorrectAnswersService {

    private final CorrectAnswersRepository repository;

    public CorrectAnswersServiceImpl(CorrectAnswersRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addCorrectAnswer(CorrectAnswers correctAnswers) {
        repository.save(correctAnswers);
    }

    @Override
    public CorrectAnswers findSolution(UUID frageId) {
        return repository.findByFrageId(frageId).orElse(null);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
