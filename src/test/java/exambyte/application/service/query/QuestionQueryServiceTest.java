package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.mapper.QuestionDTOMapper;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.QuestionService;
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
    private CorrectAnswersQueryService correctAnswersService;

    @Mock
    private QuestionDTOMapper questionDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        questionQueryService = new QuestionQueryServiceImpl(
                questionService,
                correctAnswersService,
                questionDTOMapper);

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
        when(questionDTOMapper.toDomain(questionDTOMC)).thenReturn(questionMC);
        when(questionService.addQuestion(any())).thenReturn(questionDTOMC.id());

        questionQueryService.createChoiceQuestion(questionDTOMC, "A", "A, B");

        ArgumentCaptor<CorrectAnswersDTO> captor = ArgumentCaptor.forClass(CorrectAnswersDTO.class);
        verify(correctAnswersService).addCorrectAnswers(captor.capture());

        CorrectAnswersDTO captured = captor.getValue();
        assertEquals("A", captured.solution());
        assertEquals("A, B", captured.choices());
        assertEquals(questionDTOMC.id(), captured.questionId());
    }

    @Test
    void getFreeResponseQuestionReturnsOnlyFreeResponse() {
        when(questionService.getQuestionsForExam(any())).thenReturn(List.of(questionMC, questionFreeResponse));
        when(questionDTOMapper.toDTO(questionFreeResponse)).thenReturn(questionDTOFreeResponse);

        List<QuestionDTO> result = questionQueryService.getFreeResponseQuestions(UUID.randomUUID());

        assertEquals(1, result.size());
        verify(questionDTOMapper).toDTO(questionFreeResponse);
        verify(questionDTOMapper, never()).toDTO(questionMC);
    }
}
