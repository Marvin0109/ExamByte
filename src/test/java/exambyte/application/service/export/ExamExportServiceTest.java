package exambyte.application.service.export;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.service.query.ExamQueryService;
import exambyte.application.service.query.FrageQueryService;
import exambyte.application.service.query.KorrekteAntwortenQueryService;
import exambyte.application.service.query.ProfessorQueryService;
import exambyte.domain.export_mapper.ExamExportDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ExamExportServiceTest {

    private ExamExportService service;

    private ExamDTO exam;
    private final UUID profId = UUID.randomUUID();
    private ProfessorDTO professor;
    private FrageDTO frage;
    private FrageDTO frage2;
    private KorrekteAntwortenDTO korrekteAntworten;

    @Mock
    private ExamQueryService examQueryService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private ProfessorQueryService profQueryService;

    @Mock
    private KorrekteAntwortenQueryService korrekteAntwortenQueryService;

    @Mock
    private ExamExportDTOMapper examExportDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new ExamExportServiceImpl(
                examQueryService,
                frageQueryService,
                profQueryService,
                korrekteAntwortenQueryService,
                examExportDTOMapper
        );

        exam = new ExamDTO(
                UUID.randomUUID(),
                "Title",
                profId,
                null,
                null,
                null
        );

        professor = new ProfessorDTO(
                profId,
                "Professor"
        );

        frage = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                6,
                exam.id(),
                QuestionTypeDTO.MC
        );

        frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                2,
                exam.id(),
                QuestionTypeDTO.FREITEXT
        );

        korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                frage.id()
        );
    }

    @Test
    void createExamExport() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);
        when(profQueryService.getProfessorById(profId)).thenReturn(professor);
        when(frageQueryService.getFragenForExam(exam.id())).thenReturn(List.of(frage));
        when(korrekteAntwortenQueryService.getLoesungForFrage(frage.id())).thenReturn(korrekteAntworten);
        when(examExportDTOMapper.mapDTOToExport(
                exam,
                professor.name(),
                6,
                List.of(frage),
                List.of(korrekteAntworten)))
                .thenReturn(mock());

        service.createExamExport(exam.id());

        verify(examExportDTOMapper).mapDTOToExport(
                exam,
                professor.name(),
                6,
                List.of(frage),
                List.of(korrekteAntworten));
    }

    @Test
    void createExamExport_nullKorrekteAntworten() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);
        when(profQueryService.getProfessorById(profId)).thenReturn(professor);
        when(frageQueryService.getFragenForExam(exam.id())).thenReturn(List.of(frage2));
        when(korrekteAntwortenQueryService.getLoesungForFrage(frage2.id())).thenReturn(null);

        service.createExamExport(exam.id());

        verify(examExportDTOMapper).mapDTOToExport(
                exam,
                professor.name(),
                2,
                List.of(frage2),
                List.of());
    }
}
