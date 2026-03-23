package exambyte.application.service.query;

import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.mapper.QuestionDTOMapper;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionService questionService;
    private final CorrectAnswersService correctAnswersService;
    private final QuestionDTOMapper questionDTOMapper;

    public QuestionQueryServiceImpl(QuestionService questionService,
                                    CorrectAnswersService correctAnswersService,
                                    QuestionDTOMapper questionDTOMapper) {
        this.questionService = questionService;
        this.questionDTOMapper = questionDTOMapper;
        this.correctAnswersService = correctAnswersService;
    }

    @Override
    public List<QuestionDTO> getQuestionsForExam(UUID examId) {
        return questionDTOMapper.toQuestionDTOList(questionService.getQuestionsForExam(examId));
    }

    @Override
    public void createQuestion(QuestionDTO questionDTO) {
        questionService.addQuestion(questionDTOMapper.toDomain(questionDTO));
    }

    @Override
    public void createChoiceQuestion(QuestionDTO questionDTO, String correctAnswer, String choices) {
        UUID questionId = questionService.addQuestion(questionDTOMapper.toDomain(questionDTO));
        CorrectAnswersDTO dto = new CorrectAnswersDTO(null, correctAnswer, choices, questionId);
        correctAnswersService.addCorrectAnswers(dto);
    }

    @Override
    public String getChoiceForQuestion(UUID questionId) {
        return correctAnswersService.getCorrectAnswerForQuestion(questionId).choices();
    }

    @Override
    public List<QuestionDTO> getFreeResponseQuestions(UUID examId) {
        List<Question> questions = questionService.getQuestionsForExam(examId);

        return questions.stream()
                .filter(question -> QuestionType.FREE_RESPONSE == question.getType())
                .map(questionDTOMapper::toDTO)
                .toList();
    }

    @Override
    public Map<UUID, QuestionDTO> getQuestionUUIDMap(UUID examId) {
        return questionService.getQuestionsForExam(examId).stream()
                .map(questionDTOMapper::toDTO)
                .collect(Collectors.toMap(QuestionDTO::id, q -> q));
    }

    @Override
    public QuestionDTO getQuestion(UUID questionId) {
        Optional<Question> question = questionService.getQuestion(questionId);
        return question.map(questionDTOMapper::toDTO).orElse(null);
    }
}
