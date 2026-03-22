package exambyte.application.service.export;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.service.query.ExamQueryService;
import exambyte.application.service.query.QuestionQueryService;
import exambyte.application.service.query.CorrectAnswersQueryService;
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
    private QuestionDTO frage;
    private QuestionDTO frage2;
    private CorrectAnswersDTO correctAnswers;

    @Mock
    private ExamQueryService examQueryService;

    @Mock
    private QuestionQueryService questionQueryService;

    @Mock
    private ProfessorQueryService profQueryService;

    @Mock
    private CorrectAnswersQueryService correctAnswersQueryService;

    @Mock
    private ExamExportDTOMapper examExportDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new ExamExportServiceImpl(
                examQueryService,
                questionQueryService,
                profQueryService,
                correctAnswersQueryService,
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

        frage = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                6,
                exam.id(),
                QuestionTypeDTO.MC
        );

        frage2 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 2",
                2,
                exam.id(),
                QuestionTypeDTO.FREE_RESPONSE
        );

        correctAnswers = new CorrectAnswersDTO(
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
        when(questionQueryService.getQuestionsForExam(exam.id())).thenReturn(List.of(frage));
        when(correctAnswersQueryService.getSolutionForFrage(frage.id())).thenReturn(correctAnswers);
        when(examExportDTOMapper.mapDTOToExport(
                exam,
                professor.name(),
                6,
                List.of(frage),
                List.of(correctAnswers)))
                .thenReturn(mock());

        service.createExamExport(exam.id());

        verify(examExportDTOMapper).mapDTOToExport(
                exam,
                professor.name(),
                6,
                List.of(frage),
                List.of(correctAnswers));
    }

    @Test
    void createExamExport_nullCorrectAnswers() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);
        when(profQueryService.getProfessorById(profId)).thenReturn(professor);
        when(questionQueryService.getQuestionsForExam(exam.id())).thenReturn(List.of(frage2));
        when(correctAnswersQueryService.getSolutionForFrage(frage2.id())).thenReturn(null);

        service.createExamExport(exam.id());

        verify(examExportDTOMapper).mapDTOToExport(
                exam,
                professor.name(),
                2,
                List.of(frage2),
                List.of());
    }
}
