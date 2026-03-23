package exambyte.application.service.query;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.exam.CorrectAnswers;
import exambyte.domain.repository.CorrectAnswersRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CorrectAnswersQueryServiceImpl implements CorrectAnswersQueryService {

    private final CorrectAnswersRepository repository;
    private final CorrectAnswersDTOMapper mapper;

    public CorrectAnswersQueryServiceImpl(CorrectAnswersRepository repository,
                                          CorrectAnswersDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CorrectAnswersDTO getCorrectAnswerForQuestion(UUID questionId) {
        CorrectAnswers correctAnswers = findSolution(questionId);
        return correctAnswers != null ? mapper.toDTO(correctAnswers) : null;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void addCorrectAnswers(CorrectAnswersDTO correctAnswers) {
        repository.save(mapper.toDomain(correctAnswers));
    }

    private CorrectAnswers findSolution(UUID questionId) {
        return repository.findByQuestionId(questionId)
                .orElse(null);
    }
}
