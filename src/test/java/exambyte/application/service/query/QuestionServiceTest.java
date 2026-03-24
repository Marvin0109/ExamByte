package exambyte.application.service.query;

import exambyte.application.enums.QuestionTypeDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.mapper.QuestionDTOMapper;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.enums.QuestionType;
import exambyte.domain.repository.QuestionRepository;
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

class QuestionServiceTest {

    private QuestionService questionService;

    private QuestionDTO questionDTOFreeResponse;
    private QuestionDTO questionDTOMC;
    private Question questionMC;
    private Question questionFreeResponse;

    @Mock
    private QuestionRepository repository;

    @Mock
    private CorrectAnswersService correctAnswersService;

    @Mock
    private QuestionDTOMapper questionDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        questionService = new QuestionServiceImpl(
                repository,
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
        when(repository.save(any())).thenReturn(questionDTOMC.id());

        questionService.createChoiceQuestion(questionDTOMC, "A", "A, B");

        ArgumentCaptor<CorrectAnswersDTO> captor = ArgumentCaptor.forClass(CorrectAnswersDTO.class);
        verify(correctAnswersService).addCorrectAnswers(captor.capture());

        CorrectAnswersDTO captured = captor.getValue();
        assertEquals("A", captured.solution());
        assertEquals("A, B", captured.choices());
        assertEquals(questionDTOMC.id(), captured.questionId());
    }

    @Test
    void getFreeResponseQuestionReturnsOnlyFreeResponse() {
        when(repository.findByExamId(any())).thenReturn(List.of(questionMC, questionFreeResponse));
        when(questionDTOMapper.toDTO(questionFreeResponse)).thenReturn(questionDTOFreeResponse);

        List<QuestionDTO> result = questionService.getFreeResponseQuestions(UUID.randomUUID());

        assertEquals(1, result.size());
        verify(questionDTOMapper).toDTO(questionFreeResponse);
        verify(questionDTOMapper, never()).toDTO(questionMC);
    }
}
