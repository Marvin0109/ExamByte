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

    private FrageDTO frageDTOFreitext;
    private FrageDTO frageDTOMC;
    private Frage frageMC;
    private Frage frageFreitext;

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

        frageDTOFreitext = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                10,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.FREITEXT);

        frageFreitext = new Frage.FrageBuilder()
                .fachId(frageDTOFreitext.fachId())
                .frageText("Frage")
                .maxPunkte(10)
                .professorUUID(frageDTOFreitext.profUUID())
                .examUUID(frageDTOFreitext.examUUID())
                .type(QuestionType.FREITEXT)
                .build();

        frageDTOMC = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                10,
                UUID.randomUUID(),
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        frageMC = new Frage.FrageBuilder()
                .fachId(frageDTOMC.fachId())
                .frageText("Frage")
                .maxPunkte(10)
                .professorUUID(frageDTOMC.profUUID())
                .examUUID(frageDTOMC.examUUID())
                .build();
    }

    @Test
    void createChoiceFrageWithCorrectParams() {
        KorrekteAntworten domain = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .frageFachId(frageDTOMC.fachId())
                .loesungen("A")
                .antwortOptionen("A, B")
                .build();

        when(frageDTOMapper.toDomain(frageDTOMC)).thenReturn(frageMC);
        when(frageService.addFrage(any())).thenReturn(frageDTOMC.fachId());
        when(korrekteAntwortenDTOMapper.toDomain(any())).thenReturn(domain);

        frageQueryService.createChoiceFrage(frageDTOMC, "A", "A, B");

        ArgumentCaptor<KorrekteAntworten> captor = ArgumentCaptor.forClass(KorrekteAntworten.class);
        verify(korrekteAntwortenService).addKorrekteAntwort(captor.capture());

        KorrekteAntworten captured = captor.getValue();
        assertEquals("A", captured.getLoesungen());
        assertEquals("A, B", captured.getAntwortOptionen());
        assertEquals(frageDTOMC.fachId(), captured.getFrageFachId());
    }

    @Test
    void getFreitextFragenReturnsOnlyFreitext() {
        when(frageService.getFragenForExam(any())).thenReturn(List.of(frageMC, frageFreitext));
        when(frageDTOMapper.toDTO(frageFreitext)).thenReturn(frageDTOFreitext);

        List<FrageDTO> result = frageQueryService.getFreitextFragen(UUID.randomUUID());

        assertEquals(1, result.size());
        verify(frageDTOMapper).toDTO(frageFreitext);
        verify(frageDTOMapper, never()).toDTO(frageMC);
    }
}
