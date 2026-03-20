package exambyte.application.service.export;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.domain.export_mapper.ReviewExportDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewExportServiceTest {

    private ReviewExportService service;

    private ExamDTO exam;
    private final UUID studentId = UUID.randomUUID();
    private FrageDTO frage;
    private FrageDTO frage2;
    private AnswerDTO antwort;
    private AnswerDTO antwort2;
    private ReviewDTO review;
    private ReviewDTO review2;

    @Mock
    private ExamQueryService examQueryService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private StudentQueryService studentQueryService;

    @Mock
    private AnswerQueryService answerQueryService;

    @Mock
    private ReviewerQueryService reviewerQueryService;

    @Mock
    private ReviewQueryService reviewQueryService;

    @Mock
    private ReviewExportDTOMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new ReviewExportServiceImpl(
                examQueryService,
                frageQueryService,
                studentQueryService,
                answerQueryService,
                reviewerQueryService,
                reviewQueryService,
                mapper
        );

        exam = new ExamDTO(
                UUID.randomUUID(),
                "Title",
                null,
                null,
                null,
                null
        );

        frage = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                4,
                exam.id(),
                QuestionTypeDTO.FREE_RESPONSE
        );

        frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                1,
                exam.id(),
                QuestionTypeDTO.SC
        );

        antwort = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                frage.id(),
                studentId,
                null
        );

        antwort2 = new AnswerDTO(
                UUID.randomUUID(),
                "A",
                frage2.id(),
                studentId,
                null
        );

        review = new ReviewDTO(
                UUID.randomUUID(),
                antwort.id(),
                UUID.randomUUID(),
                "Bewertung",
                4
        );

        review2 = new ReviewDTO(
                UUID.randomUUID(),
                antwort2.id(),
                UUID.randomUUID(),
                "Bewertung",
                1
        );
    }

    @Test
    void createReviewExport() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);

        when(frageQueryService.getFragenForExam(exam.id())).thenReturn(List.of(frage));

        when(studentQueryService.getStudentIdByName(any())).thenReturn(studentId);

        when(answerQueryService.findByStudentAndFrage(studentId, frage.id())).thenReturn(antwort);

        when(reviewQueryService.getReviewByAnswerId(antwort.id())).thenReturn(review);

        when(reviewerQueryService.getReviewerById(review.reviewerId()))
            .thenReturn(new ReviewerDTO(UUID.randomUUID(), "Reviewer"));

        service.createReviewExport(exam.id(), "Student");

        verify(mapper).mapDTOToExport(
                exam,
                "Reviewer",
                4,
                List.of(frage),
                List.of(antwort),
                List.of(review));
    }

    @Test
    void createReviewExport_excludingAutomaticReviewerName() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);

        when(frageQueryService.getFragenForExam(exam.id())).thenReturn(List.of(frage, frage2));

        when(studentQueryService.getStudentIdByName(any())).thenReturn(studentId);

        when(answerQueryService.findByStudentAndFrage(studentId, frage.id())).thenReturn(antwort);
        when(answerQueryService.findByStudentAndFrage(studentId, frage2.id())).thenReturn(antwort2);

        when(reviewQueryService.getReviewByAnswerId(antwort.id())).thenReturn(review);
        when(reviewQueryService.getReviewByAnswerId(antwort2.id())).thenReturn(review2);

        when(reviewerQueryService.getReviewerById(review.reviewerId()))
            .thenReturn(new ReviewerDTO(UUID.randomUUID(), "Reviewer"));
        when(reviewerQueryService.getReviewerById(review2.reviewerId()))
            .thenReturn(new ReviewerDTO(UUID.randomUUID(), "Auto reviewer"));

        service.createReviewExport(exam.id(), "Student");

        verify(mapper).mapDTOToExport(
                exam,
                "Reviewer",
                5,
                List.of(frage, frage2),
                List.of(antwort, antwort2),
                List.of(review, review2));
    }
}
