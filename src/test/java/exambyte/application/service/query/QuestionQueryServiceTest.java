package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.domain.mapper.QuestionDTOMapper;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.QuestionService;
import exambyte.domain.service.CorrectAnswersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionQueryServiceTest {

    private QuestionQueryService questionQueryService;

    private QuestionDTO questionDTOFreeResponse;
    private QuestionDTO questionDTOMC;
    private Question questionMC;
    private Question questionFreeResponse;

    @Mock
    private QuestionService questionService;

    @Mock
    private CorrectAnswersService correctAnswersService;

    @Mock
    private QuestionDTOMapper questionDTOMapper;

    @Mock
    private CorrectAnswersDTOMapper correctAnswersDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        questionQueryService = new QuestionQueryServiceImpl(
                questionService,
                correctAnswersService,
                questionDTOMapper,
                correctAnswersDTOMapper);

        questionDTOFreeResponse = new QuestionDTO(
                UUID.randomUUID(),
                "Question",
                10,
                UUID.randomUUID(),
                QuestionTypeDTO.FREE_RESPONSE);

        questionFreeResponse = new Question.FrageBuilder()
                .id(questionDTOFreeResponse.id())
                .text("Question")
                .points(10)
                .examId(questionDTOFreeResponse.examId())
                .type(QuestionType.FREE_RESPONSE)
                .build();

        questionDTOMC = new QuestionDTO(
                UUID.randomUUID(),
                "Question",
                10,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        questionMC = new Question.FrageBuilder()
                .id(questionDTOMC.id())
                .text("Question")
                .points(10)
                .examId(questionDTOMC.examId())
                .build();
    }

    @Test
    void createChoiceQuestionWithCorrectParams() {
        CorrectAnswers domain = new CorrectAnswers.CorrectAnswersBuilder()
                .frageId(questionDTOMC.id())
                .solution("A")
                .choices("A, B")
                .build();

        when(questionDTOMapper.toDomain(questionDTOMC)).thenReturn(questionMC);
        when(questionService.addQuestion(any())).thenReturn(questionDTOMC.id());
        when(correctAnswersDTOMapper.toDomain(any())).thenReturn(domain);

        questionQueryService.createChoiceQuestion(questionDTOMC, "A", "A, B");

        ArgumentCaptor<CorrectAnswers> captor = ArgumentCaptor.forClass(CorrectAnswers.class);
        verify(correctAnswersService).addCorrectAnswer(captor.capture());

        CorrectAnswers captured = captor.getValue();
        assertEquals("A", captured.getSolution());
        assertEquals("A, B", captured.getChoices());
        assertEquals(questionDTOMC.id(), captured.getFrageId());
    }

    @Test
    void getFreeResponseFragenReturnsOnlyFreeResponse() {
        when(questionService.getQuestionsForExam(any())).thenReturn(List.of(questionMC, questionFreeResponse));
        when(questionDTOMapper.toDTO(questionFreeResponse)).thenReturn(questionDTOFreeResponse);

        List<QuestionDTO> result = questionQueryService.getFreeResponseQuestions(UUID.randomUUID());

        assertEquals(1, result.size());
        verify(questionDTOMapper).toDTO(questionFreeResponse);
        verify(questionDTOMapper, never()).toDTO(questionMC);
    }
}
