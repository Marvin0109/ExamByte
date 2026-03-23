package exambyte.application.service.export;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.application.mapper.export.ReviewExportDTOMapper;
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
    private QuestionDTO question1;
    private QuestionDTO question2;
    private AnswerDTO answer1;
    private AnswerDTO answer2;
    private ReviewDTO review;
    private ReviewDTO review2;

    @Mock
    private ExamQueryService examQueryService;

    @Mock
    private QuestionQueryService questionQueryService;

    @Mock
    private StudentService studentService;

    @Mock
    private AnswerService answerService;

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
                questionQueryService,
                studentService,
                answerService,
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

        question1 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                4,
                exam.id(),
                QuestionTypeDTO.FREE_RESPONSE
        );

        question2 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 2",
                1,
                exam.id(),
                QuestionTypeDTO.SC
        );

        answer1 = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                question1.id(),
                studentId,
                null
        );

        answer2 = new AnswerDTO(
                UUID.randomUUID(),
                "A",
                question2.id(),
                studentId,
                null
        );

        review = new ReviewDTO(
                UUID.randomUUID(),
                answer1.id(),
                UUID.randomUUID(),
                "Text",
                4
        );

        review2 = new ReviewDTO(
                UUID.randomUUID(),
                answer2.id(),
                UUID.randomUUID(),
                "Text",
                1
        );
    }

    @Test
    void createReviewExport() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);

        when(questionQueryService.getQuestionsForExam(exam.id())).thenReturn(List.of(question1));

        when(studentService.getStudentIdByName(any())).thenReturn(studentId);

        when(answerService.findByStudentAndQuestion(studentId, question1.id())).thenReturn(answer1);

        when(reviewQueryService.getReviewByAnswerId(answer1.id())).thenReturn(review);

        when(reviewerQueryService.getReviewerById(review.reviewerId()))
            .thenReturn(new ReviewerDTO(UUID.randomUUID(), "Reviewer"));

        service.createReviewExport(exam.id(), "Student");

        verify(mapper).mapDTOToExport(
                exam,
                "Reviewer",
                4,
                List.of(question1),
                List.of(answer1),
                List.of(review));
    }

    @Test
    void createReviewExport_excludingAutomaticReviewerName() {
        when(examQueryService.getExam(exam.id())).thenReturn(exam);

        when(questionQueryService.getQuestionsForExam(exam.id())).thenReturn(List.of(question1, question2));

        when(studentService.getStudentIdByName(any())).thenReturn(studentId);

        when(answerService.findByStudentAndQuestion(studentId, question1.id())).thenReturn(answer1);
        when(answerService.findByStudentAndQuestion(studentId, question2.id())).thenReturn(answer2);

        when(reviewQueryService.getReviewByAnswerId(answer1.id())).thenReturn(review);
        when(reviewQueryService.getReviewByAnswerId(answer2.id())).thenReturn(review2);

        when(reviewerQueryService.getReviewerById(review.reviewerId()))
            .thenReturn(new ReviewerDTO(UUID.randomUUID(), "Reviewer"));
        when(reviewerQueryService.getReviewerById(review2.reviewerId()))
            .thenReturn(new ReviewerDTO(UUID.randomUUID(), "Auto reviewer"));

        service.createReviewExport(exam.id(), "Student");

        verify(mapper).mapDTOToExport(
                exam,
                "Reviewer",
                5,
                List.of(question1, question2),
                List.of(answer1, answer2),
                List.of(review, review2));
    }
}
