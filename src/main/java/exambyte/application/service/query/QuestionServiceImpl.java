package exambyte.application.service.query;

import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.mapper.QuestionDTOMapper;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository repository;
    private final CorrectAnswersService correctAnswersService;
    private final QuestionDTOMapper questionDTOMapper;

    public QuestionServiceImpl(QuestionRepository repository,
                               CorrectAnswersService correctAnswersService,
                               QuestionDTOMapper questionDTOMapper) {
        this.repository = repository;
        this.questionDTOMapper = questionDTOMapper;
        this.correctAnswersService = correctAnswersService;
    }

    @Override
    public List<QuestionDTO> getQuestionsForExam(UUID examId) {
        return questionDTOMapper.toQuestionDTOList(findQuestionsForExam(examId));
    }

    @Override
    public void createQuestion(QuestionDTO questionDTO) {
        addQuestion(questionDTOMapper.toDomain(questionDTO));
    }

    @Override
    public void createChoiceQuestion(QuestionDTO questionDTO, String correctAnswer, String choices) {
        UUID questionId = addQuestion(questionDTOMapper.toDomain(questionDTO));
        CorrectAnswersDTO dto = new CorrectAnswersDTO(null, correctAnswer, choices, questionId);
        correctAnswersService.addCorrectAnswers(dto);
    }

    @Override
    public String getChoiceForQuestion(UUID questionId) {
        return correctAnswersService.getCorrectAnswerForQuestion(questionId).choices();
    }

    @Override
    public List<QuestionDTO> getFreeResponseQuestions(UUID examId) {
        List<Question> questions = findQuestionsForExam(examId);

        return questions.stream()
                .filter(question -> QuestionType.FREE_RESPONSE == question.getType())
                .map(questionDTOMapper::toDTO)
                .toList();
    }

    @Override
    public Map<UUID, QuestionDTO> getQuestionUUIDMap(UUID examId) {
        return findQuestionsForExam(examId).stream()
                .map(questionDTOMapper::toDTO)
                .collect(Collectors.toMap(QuestionDTO::id, q -> q));
    }

    @Override
    public QuestionDTO getQuestion(UUID questionId) {
        Optional<Question> question = findQuestion(questionId);
        return question.map(questionDTOMapper::toDTO).orElse(null);
    }

    private List<Question> findQuestionsForExam(UUID examId) {
        return repository.findByExamId(examId);
    }

    private Optional<Question> findQuestion(UUID questionId) {
        return repository.findById(questionId);
    }

    private UUID addQuestion(Question question) {
        return repository.save(question);
    }
}
