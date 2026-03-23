package exambyte.infrastructure.service;

import exambyte.domain.model.exam.Question;
import exambyte.domain.repository.QuestionRepository;
import exambyte.domain.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository repository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.repository = questionRepository;
    }

    @Override
    public List<Question> getQuestionsForExam(UUID examId) {
        return repository.findByExamId(examId);
    }

    @Override
    public UUID addQuestion(Question question) {
        return repository.save(question);
    }

    @Override
    public Optional<Question> getQuestion(UUID questionId) {
        return repository.findById(questionId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
