package exambyte.application.service;

import exambyte.application.dto.FrageDTO;
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
    private FrageQueryService frageQueryService;

    @Mock
    private StudentQueryService studentQueryService;

    @Mock
    private ProfessorQueryService professorQueryService;

    @Mock
    private AnswerQueryService answerQueryService;

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
                frageQueryService,
                professorQueryService,
                reviewerQueryService,
                studentQueryService,
                answerQueryService,
                reviewQueryService,
                correctAnswersQueryService);
    }

    @Test
    void createChoiceFrageWithCorrectParams() {
        FrageDTO mockFrageDTO = mock(FrageDTO.class);
        examFacadeService.createChoiceFrage(mockFrageDTO, "A", "A, B");
        verify(frageQueryService).createChoiceFrage(mockFrageDTO, "A", "A, B");
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
        examFacadeService.createReview("Bewertung", 1, answerId, reviewerId);
        verify(reviewQueryService).createReview("Bewertung", 1, answerId, reviewerId);
    }
}
