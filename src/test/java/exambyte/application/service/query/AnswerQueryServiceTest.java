package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.AnswerDTOMapper;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerQueryServiceTest {

    private AnswerQueryService answerQueryService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private AnswerService answerService;

    @Mock
    private AnswerDTOMapper answerDTOMapper;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID FRAGE1_ID = UUID.randomUUID();
    private static final UUID FRAGE2_ID = UUID.randomUUID();
    private static final LocalDateTime TIME =  LocalDateTime.of(2000, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        answerQueryService = new AnswerQueryServiceImpl(frageQueryService, answerService, answerDTOMapper);
    }

    @Test
    void saveAnswers_success() {
        // Arrange
        FrageDTO frage1 = new FrageDTO(
                FRAGE1_ID,
                "Frage",
                10.0,
                null,
                QuestionTypeDTO.MC
        );

        FrageDTO frage2 = new FrageDTO(
                FRAGE2_ID,
                "Frage",
                5.0,
                null,
                QuestionTypeDTO.FREE_RESPONSE
        );

        Map<String, List<String>> answerMap = Map.of(
                FRAGE1_ID.toString(), List.of("Answer 1", "Answer 2"),
                FRAGE2_ID.toString(), List.of("Answer A, Answer B\nAnswer C")
        );

        AnswerDTO dto1 = new AnswerDTO(null, "Answer 1\nAnswer 2", FRAGE1_ID, STUDENT_ID, TIME);
        AnswerDTO dto2 = new AnswerDTO(null, "Answer A", FRAGE2_ID, STUDENT_ID, TIME);

        when(answerService.findByStudentAndFrage(FRAGE1_ID, STUDENT_ID)).thenReturn(null);
        when(answerService.findByStudentAndFrage(FRAGE2_ID, STUDENT_ID)).thenReturn(null);
        when(answerDTOMapper.toDomain(dto1)).thenReturn(mock());
        when(answerDTOMapper.toDomain(dto2)).thenReturn(mock());
        when(frageQueryService.getFrage(FRAGE1_ID)).thenReturn(frage1);
        when(frageQueryService.getFrage(FRAGE2_ID)).thenReturn(frage2);

        // Act
        boolean result = answerQueryService.saveAnswers(STUDENT_ID, answerMap);

        // Assert
        assertTrue(result);
        verify(answerService, times(2)).addAnswer(any());
    }

    @Test
    void saveAnswers_exception() {
        // Arrange
        Map<String, List<String>> answerMap = Map.of(
                FRAGE1_ID.toString(), List.of("Answer 1")
        );

        when(answerDTOMapper.toDomain(any()))
                .thenThrow(new RuntimeException("Error Message"));

        // Act
        assertThrows(Exception.class, () -> answerQueryService.saveAnswers(STUDENT_ID, answerMap));

        // Assert
        verify(answerService, never()).addAnswer(any());
    }

    @Test
    void getAnswers_success() {
        // Arrange
        Answer answer = mock(Answer.class);
        AnswerDTO dto = mock(AnswerDTO.class);

        when(answerService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID))
                .thenReturn(answer);
        when(answerService.findByStudentAndFrage(STUDENT_ID, FRAGE2_ID))
                .thenReturn(null);

        when(answerDTOMapper.toDTO(answer)).thenReturn(dto);

        // Act
        List<AnswerDTO> result = answerQueryService.getAnswers(
                STUDENT_ID,
                Set.of(FRAGE1_ID, FRAGE2_ID)
        );

        // Assert
        assertThat(result).hasSize(1).contains(dto);

        verify(answerService, times(2))
                .findByStudentAndFrage(eq(STUDENT_ID), any());

        verify(answerDTOMapper).toDTO(answer);
        verifyNoMoreInteractions(answerDTOMapper);
    }

    @Test
    void getAnswers_empty() {
        // Arrange
        when(answerService.findByStudentAndFrage(any(), any()))
                .thenReturn(null);

        // Act
        List<AnswerDTO> result = answerQueryService.getAnswers(
                STUDENT_ID,
                Set.of(FRAGE1_ID)
        );

        // Assert
        assertThat(result).isEmpty();
        verify(answerDTOMapper, never()).toDTO(any());
    }

    @Test
    void getFreeResponseSolutionForExam_success() {
        // Arrange
        UUID examId = UUID.randomUUID();

        FrageDTO frageDTO = mock(FrageDTO.class);
        Answer answer = mock(Answer.class);
        AnswerDTO dto = mock(AnswerDTO.class);

        when(frageDTO.id()).thenReturn(FRAGE1_ID);
        when(frageQueryService.getFreeResponseFragen(examId))
                .thenReturn(List.of(frageDTO));

        when(answerService.findByFrageId(FRAGE1_ID))
                .thenReturn(answer);

        when(answerDTOMapper.toDTO(answer))
                .thenReturn(dto);

        // Act
        List<AnswerDTO> result = answerQueryService.getFreeResponseAnswersForExam(examId);

        // Assert
        assertThat(result).containsExactly(dto);
    }

    @Test
    void getFreeResponseAnswersForExam_nullFiltered() {
        // Arrange
        UUID examId = UUID.randomUUID();

        FrageDTO frageDTO = mock(FrageDTO.class);
        when(frageDTO.id()).thenReturn(FRAGE1_ID);

        when(frageQueryService.getFreeResponseFragen(examId))
                .thenReturn(List.of(frageDTO));

        when(answerService.findByFrageId(FRAGE1_ID))
                .thenReturn(null);

        // Act
        List<AnswerDTO> result = answerQueryService.getFreeResponseAnswersForExam(examId);

        // Assert
        assertThat(result).isEmpty();
        verify(answerDTOMapper, never()).toDTO(any());
    }

    @Test
    void findByStudentAndFrage_success() {
        // Arrange
        Answer domainAntwort = mock(Answer.class);
        AnswerDTO dto = mock(AnswerDTO.class);

        when(answerService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID))
                .thenReturn(domainAntwort);
        when(answerDTOMapper.toDTO(domainAntwort))
                .thenReturn(dto);

        // Act
        AnswerDTO result =
                answerQueryService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);

        // Assert
        assertThat(result).isEqualTo(dto);

        verify(answerService, times(2))
                .findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);
        verify(answerDTOMapper)
                .toDTO(domainAntwort);
    }

    @Test
    void findByStudentAndFrage_notFound() {
        when(answerService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID))
                .thenReturn(null);

        AnswerDTO result = answerQueryService.findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);

        assertThat(result).isNull();

        verify(answerService).findByStudentAndFrage(STUDENT_ID, FRAGE1_ID);
    }
}
