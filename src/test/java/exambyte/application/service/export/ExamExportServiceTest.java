package exambyte.application.service.export;

import exambyte.application.enums.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.service.query.ExamService;
import exambyte.application.service.query.QuestionService;
import exambyte.application.service.query.CorrectAnswersService;
import exambyte.application.service.query.ProfessorService;
import exambyte.application.mapper.export.ExamExportDTOMapper;
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
    private QuestionDTO question1;
    private QuestionDTO question2;
    private CorrectAnswersDTO correctAnswers;

    @Mock
    private ExamService examService;

    @Mock
    private QuestionService questionService;

    @Mock
    private ProfessorService profQueryService;

    @Mock
    private CorrectAnswersService correctAnswersService;

    @Mock
    private ExamExportDTOMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new ExamExportServiceImpl(
                examService,
                questionService,
                profQueryService,
                correctAnswersService,
                mapper
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

        question1 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                6,
                exam.id(),
                QuestionTypeDTO.MC
        );

        question2 = new QuestionDTO(
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
                question1.id()
        );
    }

    @Test
    void createExamExport() {
        when(examService.getExam(exam.id())).thenReturn(exam);
        when(profQueryService.getProfessorById(profId)).thenReturn(professor);
        when(questionService.getQuestionsForExam(exam.id())).thenReturn(List.of(question1));
        when(correctAnswersService.getCorrectAnswerForQuestion(question1.id())).thenReturn(correctAnswers);
        when(mapper.mapDTOToExport(
                exam,
                professor.name(),
                6,
                List.of(question1),
                List.of(correctAnswers)))
                .thenReturn(mock());

        service.createExamExport(exam.id());

        verify(mapper).mapDTOToExport(
                exam,
                professor.name(),
                6,
                List.of(question1),
                List.of(correctAnswers));
    }

    @Test
    void createExamExport_nullCorrectAnswers() {
        when(examService.getExam(exam.id())).thenReturn(exam);
        when(profQueryService.getProfessorById(profId)).thenReturn(professor);
        when(questionService.getQuestionsForExam(exam.id())).thenReturn(List.of(question2));
        when(correctAnswersService.getCorrectAnswerForQuestion(question2.id())).thenReturn(null);

        service.createExamExport(exam.id());

        verify(mapper).mapDTOToExport(
                exam,
                professor.name(),
                2,
                List.of(question2),
                List.of());
    }
}
