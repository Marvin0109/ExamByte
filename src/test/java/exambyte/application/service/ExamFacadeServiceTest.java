package exambyte.application.service;

import exambyte.application.dto.QuestionDTO;
import exambyte.application.service.export.ExamExportService;
import exambyte.application.service.export.ReviewExportService;
import exambyte.application.service.query.*;
import exambyte.application.service.usecase.ReviewManagementService;
import exambyte.application.service.usecase.ExamManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ExamFacadeServiceTest {

    private ExamFacadeService examFacadeService;

    @Mock
    private ReviewManagementService reviewManagementService;

    @Mock
    private ExamManagementService examManagementService;

    @Mock
    private QuestionQueryService questionQueryService;

    @Mock
    private StudentQueryService studentQueryService;

    @Mock
    private ProfessorService professorService;

    @Mock
    private AnswerService answerService;

    @Mock
    private ReviewQueryService reviewQueryService;

    @Mock
    private ReviewerQueryService reviewerQueryService;

    @Mock
    private CorrectAnswersQueryService correctAnswersQueryService;

    @Mock
    private ExamExportService examExportService;

    @Mock
    private ReviewExportService reviewExportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        examFacadeService = new ExamFacadeServiceImpl(
                reviewManagementService,
                examManagementService,
                examExportService,
                reviewExportService,
                questionQueryService,
                professorService,
                reviewerQueryService,
                studentQueryService,
                answerService,
                reviewQueryService,
                correctAnswersQueryService);
    }

    @Test
    void createChoiceQuestionWithCorrectParams() {
        QuestionDTO mockQuestionDTO = mock(QuestionDTO.class);
        examFacadeService.createChoiceQuestion(mockQuestionDTO, "A", "A, B");
        verify(questionQueryService).createChoiceQuestion(mockQuestionDTO, "A", "A, B");
    }

    @Test
    void isSubmitBeingReviewedWithCorrectParams() {
        UUID examId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        examFacadeService.isSubmitBeingReviewed(examId, studentId);
        verify(reviewManagementService).submitHasReview(examId, studentId);
    }

    @Test
    void createReviewWithCorrectParams() {
        UUID answerId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();
        examFacadeService.createReview("Text", 1, answerId, reviewerId);
        verify(reviewQueryService).createReview("Text", 1, answerId, reviewerId);
    }
}
