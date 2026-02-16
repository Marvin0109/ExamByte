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
    private AntwortDTO antwort;
    private AntwortDTO antwort2;
    private ReviewDTO review;
    private ReviewDTO review2;

    @Mock
    private ExamQueryService examQueryService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private StudentQueryService studentQueryService;

    @Mock
    private AntwortQueryService antwortQueryService;

    @Mock
    private KorrektorQueryService korrektorQueryService;

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
                antwortQueryService,
                korrektorQueryService,
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
                null,
                exam.fachId(),
                QuestionTypeDTO.FREITEXT
        );

        frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                1,
                null,
                exam.fachId(),
                QuestionTypeDTO.SC
        );

        antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frage.fachId(),
                studentId,
                null
        );

        antwort2 = new AntwortDTO(
                UUID.randomUUID(),
                "A",
                frage2.fachId(),
                studentId,
                null
        );

        review = new ReviewDTO(
                UUID.randomUUID(),
                antwort.fachId(),
                UUID.randomUUID(),
                "Bewertung",
                4
        );

        review2 = new ReviewDTO(
                UUID.randomUUID(),
                antwort2.fachId(),
                UUID.randomUUID(),
                "Bewertung",
                1
        );
    }

    @Test
    void createReviewExport() {
        when(examQueryService.getExam(exam.fachId())).thenReturn(exam);

        when(frageQueryService.getFragenForExam(exam.fachId())).thenReturn(List.of(frage));

        when(studentQueryService.getStudentIdByName(any())).thenReturn(studentId);

        when(antwortQueryService.findByStudentAndFrage(studentId, frage.fachId())).thenReturn(antwort);

        when(reviewQueryService.getReviewByAntwortId(antwort.fachId())).thenReturn(review);

        when(korrektorQueryService.getReviewerById(review.korrektorFachId()))
                .thenReturn(new KorrektorDTO(UUID.randomUUID(), "Korrektor"));

        service.createReviewExport(exam.fachId(), "Student");

        verify(mapper).mapDTOToExport(
                exam,
                "Korrektor",
                4,
                List.of(frage),
                List.of(antwort),
                List.of(review));
    }

    @Test
    void createReviewExport_excludingAutomaticReviewerName() {
        when(examQueryService.getExam(exam.fachId())).thenReturn(exam);

        when(frageQueryService.getFragenForExam(exam.fachId())).thenReturn(List.of(frage, frage2));

        when(studentQueryService.getStudentIdByName(any())).thenReturn(studentId);

        when(antwortQueryService.findByStudentAndFrage(studentId, frage.fachId())).thenReturn(antwort);
        when(antwortQueryService.findByStudentAndFrage(studentId, frage2.fachId())).thenReturn(antwort2);

        when(reviewQueryService.getReviewByAntwortId(antwort.fachId())).thenReturn(review);
        when(reviewQueryService.getReviewByAntwortId(antwort2.fachId())).thenReturn(review2);

        when(korrektorQueryService.getReviewerById(review.korrektorFachId()))
                .thenReturn(new KorrektorDTO(UUID.randomUUID(), "Korrektor"));
        when(korrektorQueryService.getReviewerById(review2.korrektorFachId()))
                .thenReturn(new KorrektorDTO(UUID.randomUUID(), "Automatischer Korrektor"));

        service.createReviewExport(exam.fachId(), "Student");

        verify(mapper).mapDTOToExport(
                exam,
                "Korrektor",
                5,
                List.of(frage, frage2),
                List.of(antwort, antwort2),
                List.of(review, review2));
    }
}
