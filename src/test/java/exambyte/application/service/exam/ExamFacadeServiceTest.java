package exambyte.application.service.exam;

import exambyte.application.dto.FrageDTO;
import exambyte.application.service.ExamFacadeService;
import exambyte.application.service.ExamFacadeServiceImpl;
import exambyte.application.service.query.ExamQueryService;
import exambyte.application.service.query.ProfessorQueryService;
import exambyte.application.service.query.StudentQueryService;
import exambyte.application.service.query.FrageQueryService;
import exambyte.application.service.usecase.ReviewManagementService;
import exambyte.application.service.query.AntwortQueryService;
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
    private ExamQueryService examQueryService;

    @Mock
    private ProfessorQueryService professorQueryService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private ReviewManagementService reviewManagementService;

    @Mock
    private ExamManagementService examManagementService;

    @Mock
    private AntwortQueryService antwortQueryService;

    @Mock
    private StudentQueryService studentQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        examFacadeService = new ExamFacadeServiceImpl(
                examQueryService,
                professorQueryService,
                frageQueryService,
                reviewManagementService,
                examManagementService,
                antwortQueryService,
                studentQueryService);
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
        UUID antwortId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();
        examFacadeService.createReview("Bewertung", 1, antwortId, korrektorId);
        verify(reviewManagementService).createReview("Bewertung", 1, antwortId, korrektorId);
    }
}
