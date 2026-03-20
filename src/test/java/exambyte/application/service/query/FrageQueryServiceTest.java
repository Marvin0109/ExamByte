package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.FrageService;
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

class FrageQueryServiceTest {

    private FrageQueryService frageQueryService;

    private FrageDTO frageDTOFreeResponse;
    private FrageDTO frageDTOMC;
    private Frage frageMC;
    private Frage frageFreeResponse;

    @Mock
    private FrageService frageService;

    @Mock
    private CorrectAnswersService correctAnswersService;

    @Mock
    private FrageDTOMapper frageDTOMapper;

    @Mock
    private CorrectAnswersDTOMapper correctAnswersDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        frageQueryService = new FrageQueryServiceImpl(
                frageService,
                correctAnswersService,
                frageDTOMapper,
                correctAnswersDTOMapper);

        frageDTOFreeResponse = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                10,
                UUID.randomUUID(),
                QuestionTypeDTO.FREE_RESPONSE);

        frageFreeResponse = new Frage.FrageBuilder()
                .id(frageDTOFreeResponse.id())
                .frageText("Frage")
                .maxPunkte(10)
                .examId(frageDTOFreeResponse.examId())
                .type(QuestionType.FREE_RESPONSE)
                .build();

        frageDTOMC = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                10,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        frageMC = new Frage.FrageBuilder()
                .id(frageDTOMC.id())
                .frageText("Frage")
                .maxPunkte(10)
                .examId(frageDTOMC.examId())
                .build();
    }

    @Test
    void createChoiceFrageWithCorrectParams() {
        CorrectAnswers domain = new CorrectAnswers.CorrectAnswersBuilder()
                .frageId(frageDTOMC.id())
                .solution("A")
                .choices("A, B")
                .build();

        when(frageDTOMapper.toDomain(frageDTOMC)).thenReturn(frageMC);
        when(frageService.addFrage(any())).thenReturn(frageDTOMC.id());
        when(correctAnswersDTOMapper.toDomain(any())).thenReturn(domain);

        frageQueryService.createChoiceFrage(frageDTOMC, "A", "A, B");

        ArgumentCaptor<CorrectAnswers> captor = ArgumentCaptor.forClass(CorrectAnswers.class);
        verify(correctAnswersService).addCorrectAnswer(captor.capture());

        CorrectAnswers captured = captor.getValue();
        assertEquals("A", captured.getSolution());
        assertEquals("A, B", captured.getChoices());
        assertEquals(frageDTOMC.id(), captured.getFrageId());
    }

    @Test
    void getFreeResponseFragenReturnsOnlyFreeResponse() {
        when(frageService.getFragenForExam(any())).thenReturn(List.of(frageMC, frageFreeResponse));
        when(frageDTOMapper.toDTO(frageFreeResponse)).thenReturn(frageDTOFreeResponse);

        List<FrageDTO> result = frageQueryService.getFreeResponseFragen(UUID.randomUUID());

        assertEquals(1, result.size());
        verify(frageDTOMapper).toDTO(frageFreeResponse);
        verify(frageDTOMapper, never()).toDTO(frageMC);
    }
}
