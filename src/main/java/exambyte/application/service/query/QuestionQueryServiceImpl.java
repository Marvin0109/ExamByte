package exambyte.application.service.query;

import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.domain.mapper.QuestionDTOMapper;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.QuestionService;
import exambyte.domain.service.CorrectAnswersService;
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
    private final CorrectAnswersDTOMapper correctAnswersDTOMapper;

    public QuestionQueryServiceImpl(QuestionService questionService,
                                    CorrectAnswersService correctAnswersService,
                                    QuestionDTOMapper questionDTOMapper,
                                    CorrectAnswersDTOMapper correctAnswersDTOMapper) {
        this.questionService = questionService;
        this.questionDTOMapper = questionDTOMapper;
        this.correctAnswersService = correctAnswersService;
        this.correctAnswersDTOMapper = correctAnswersDTOMapper;
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
        UUID frageId = questionService.addQuestion(questionDTOMapper.toDomain(questionDTO));
        CorrectAnswersDTO dto = new CorrectAnswersDTO(null, correctAnswer, choices, frageId);
        correctAnswersService.addCorrectAnswer(correctAnswersDTOMapper.toDomain(dto));
    }

    @Override
    public String getChoiceForQuestion(UUID questionId) {
        return correctAnswersService.findSolution(questionId).getChoices();
    }

    @Override
    public List<QuestionDTO> getFreeResponseQuestions(UUID examId) {
        List<Question> fragen = questionService.getQuestionsForExam(examId);

        return fragen.stream()
                .filter(frage -> QuestionType.FREE_RESPONSE == frage.getType())
                .map(questionDTOMapper::toDTO)
                .toList();
    }

    @Override
    public Map<UUID, QuestionDTO> getQuestionUUIDMap(UUID examId) {
        return questionService.getQuestionsForExam(examId).stream()
                .map(questionDTOMapper::toDTO)
                .collect(Collectors.toMap(QuestionDTO::id, f -> f));
    }

    @Override
    public QuestionDTO getQuestion(UUID questionId) {
        Optional<Question> frage = questionService.getQuestion(questionId);
        return frage.map(questionDTOMapper::toDTO).orElse(null);
    }
}
