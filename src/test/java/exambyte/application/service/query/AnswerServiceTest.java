package exambyte.application.service.query;

import exambyte.application.enums.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.mapper.AnswerDTOMapper;
import exambyte.domain.model.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerServiceTest {

    private AnswerService answerService;

    @Mock
    private QuestionService questionService;

    @Mock
    private AnswerRepository repository;

    @Mock
    private AnswerDTOMapper mapper;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID QUESTION_1_ID = UUID.randomUUID();
    private static final UUID QUESTION_2_ID = UUID.randomUUID();
    private static final LocalDateTime TIME =  LocalDateTime.of(2000, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        answerService = new AnswerServiceImpl(questionService, repository, mapper);
    }

    @Test
    void saveAnswers_success() {
        // Arrange
        QuestionDTO frage1 = new QuestionDTO(
                QUESTION_1_ID,
                "Question",
                10.0,
                null,
                QuestionTypeDTO.MC
        );

        QuestionDTO frage2 = new QuestionDTO(
                QUESTION_2_ID,
                "Question",
                5.0,
                null,
                QuestionTypeDTO.FREE_RESPONSE
        );

        Map<String, List<String>> answerMap = Map.of(
                QUESTION_1_ID.toString(), List.of("Answer 1", "Answer 2"),
                QUESTION_2_ID.toString(), List.of("Answer A, Answer B\nAnswer C")
        );

        AnswerDTO dto1 = new AnswerDTO(null, "Answer 1\nAnswer 2", QUESTION_1_ID, STUDENT_ID, TIME);
        AnswerDTO dto2 = new AnswerDTO(null, "Answer A", QUESTION_2_ID, STUDENT_ID, TIME);

        when(repository.findByStudentIdAndQuestionId(QUESTION_1_ID, STUDENT_ID)).thenReturn(null);
        when(repository.findByStudentIdAndQuestionId(QUESTION_2_ID, STUDENT_ID)).thenReturn(null);
        when(mapper.toDomain(dto1)).thenReturn(mock());
        when(mapper.toDomain(dto2)).thenReturn(mock());
        when(questionService.getQuestion(QUESTION_1_ID)).thenReturn(frage1);
        when(questionService.getQuestion(QUESTION_2_ID)).thenReturn(frage2);

        // Act
        boolean result = answerService.saveAnswers(STUDENT_ID, answerMap);

        // Assert
        assertTrue(result);
        verify(repository, times(2)).save(any());
    }

    @Test
    void saveAnswers_exception() {
        // Arrange
        Map<String, List<String>> answerMap = Map.of(
                QUESTION_1_ID.toString(), List.of("Answer 1")
        );

        when(mapper.toDomain(any()))
                .thenThrow(new RuntimeException("Error Message"));

        // Act
        assertThrows(Exception.class, () -> answerService.saveAnswers(STUDENT_ID, answerMap));

        // Assert
        verify(repository, never()).save(any());
    }

    @Test
    void getAnswers_success() {
        // Arrange
        Answer answer = mock(Answer.class);
        AnswerDTO dto = mock(AnswerDTO.class);

        when(repository.findByStudentIdAndQuestionId(STUDENT_ID, QUESTION_1_ID))
                .thenReturn(Optional.of(answer));
        when(repository.findByStudentIdAndQuestionId(STUDENT_ID, QUESTION_2_ID))
                .thenReturn(Optional.empty());

        when(mapper.toDTO(answer)).thenReturn(dto);

        // Act
        List<AnswerDTO> result = answerService.getAnswers(
                STUDENT_ID,
                Set.of(QUESTION_1_ID, QUESTION_2_ID)
        );

        // Assert
        assertThat(result).hasSize(1).contains(dto);

        verify(repository, times(2))
                .findByStudentIdAndQuestionId(eq(STUDENT_ID), any());

        verify(mapper).toDTO(answer);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void getAnswers_empty() {
        // Arrange
        when(repository.findByStudentIdAndQuestionId(any(), any()))
                .thenReturn(Optional.empty());

        // Act
        List<AnswerDTO> result = answerService.getAnswers(
                STUDENT_ID,
                Set.of(QUESTION_1_ID)
        );

        // Assert
        assertThat(result).isEmpty();
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void getFreeResponseSolutionForExam_success() {
        // Arrange
        UUID examId = UUID.randomUUID();

        QuestionDTO questionDTO = mock(QuestionDTO.class);
        Answer answer = mock(Answer.class);
        AnswerDTO dto = mock(AnswerDTO.class);

        when(questionDTO.id()).thenReturn(QUESTION_1_ID);
        when(questionService.getFreeResponseQuestions(examId))
                .thenReturn(List.of(questionDTO));

        when(repository.findByQuestionId(QUESTION_1_ID))
                .thenReturn(answer);

        when(mapper.toDTO(answer))
                .thenReturn(dto);

        // Act
        List<AnswerDTO> result = answerService.getFreeResponseAnswersForExam(examId);

        // Assert
        assertThat(result).containsExactly(dto);
    }

    @Test
    void getFreeResponseAnswersForExam_nullFiltered() {
        // Arrange
        UUID examId = UUID.randomUUID();

        QuestionDTO questionDTO = mock(QuestionDTO.class);
        when(questionDTO.id()).thenReturn(QUESTION_1_ID);

        when(questionService.getFreeResponseQuestions(examId))
                .thenReturn(List.of(questionDTO));

        when(repository.findByQuestionId(QUESTION_1_ID))
                .thenReturn(null);

        // Act
        List<AnswerDTO> result = answerService.getFreeResponseAnswersForExam(examId);

        // Assert
        assertThat(result).isEmpty();
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void findByStudentAndQuestion_success() {
        // Arrange
        Answer answer = mock(Answer.class);
        AnswerDTO dto = mock(AnswerDTO.class);

        when(repository.findByStudentIdAndQuestionId(STUDENT_ID, QUESTION_1_ID))
                .thenReturn(Optional.of(answer));
        when(mapper.toDTO(answer))
                .thenReturn(dto);

        // Act
        AnswerDTO result =
                answerService.findByStudentAndQuestion(STUDENT_ID, QUESTION_1_ID);

        // Assert
        assertThat(result).isEqualTo(dto);

        verify(repository, times(2))
                .findByStudentIdAndQuestionId(STUDENT_ID, QUESTION_1_ID);
        verify(mapper)
                .toDTO(answer);
    }

    @Test
    void findByStudentAndQuestion_notFound() {
        when(repository.findByStudentIdAndQuestionId(STUDENT_ID, QUESTION_1_ID))
                .thenReturn(Optional.empty());

        AnswerDTO result = answerService.findByStudentAndQuestion(STUDENT_ID, QUESTION_1_ID);

        assertThat(result).isNull();

        verify(repository).findByStudentIdAndQuestionId(STUDENT_ID, QUESTION_1_ID);
    }
}
