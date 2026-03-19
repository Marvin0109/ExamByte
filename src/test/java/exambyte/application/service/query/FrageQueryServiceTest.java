package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.FrageService;
import exambyte.domain.service.KorrekteAntwortenService;
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
    private KorrekteAntwortenService korrekteAntwortenService;

    @Mock
    private FrageDTOMapper frageDTOMapper;

    @Mock
    private KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        frageQueryService = new FrageQueryServiceImpl(
                frageService,
                korrekteAntwortenService,
                frageDTOMapper,
                korrekteAntwortenDTOMapper);

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
        KorrekteAntworten domain = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .frageId(frageDTOMC.id())
                .loesungen("A")
                .antwortOptionen("A, B")
                .build();

        when(frageDTOMapper.toDomain(frageDTOMC)).thenReturn(frageMC);
        when(frageService.addFrage(any())).thenReturn(frageDTOMC.id());
        when(korrekteAntwortenDTOMapper.toDomain(any())).thenReturn(domain);

        frageQueryService.createChoiceFrage(frageDTOMC, "A", "A, B");

        ArgumentCaptor<KorrekteAntworten> captor = ArgumentCaptor.forClass(KorrekteAntworten.class);
        verify(korrekteAntwortenService).addKorrekteAntwort(captor.capture());

        KorrekteAntworten captured = captor.getValue();
        assertEquals("A", captured.getLoesungen());
        assertEquals("A, B", captured.getAntwortOptionen());
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
