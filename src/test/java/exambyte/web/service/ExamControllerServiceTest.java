package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamFacadeService;
import exambyte.web.common.QuestionTypeWeb;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.create_exam.QuestionData;
import exambyte.web.form.info.ReviewCoverageForm;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamControllerServiceTest {

    @Mock
    private ExamFacadeService examFacadeService;

    @Mock
    private HelperService helperService;

    private ExamControllerService service;

    private ExamDTO exam;
    private static final UUID EXAM_ID = UUID.randomUUID();
    private static final UUID PROF_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ExamControllerServiceImpl(examFacadeService, helperService);

        exam = new ExamDTO(
                EXAM_ID,
                "Exam",
                PROF_ID,
                null,
                null,
                null
        );
    }

    @Test
    void createExamForm_success() {
        // Act
        ExamForm form = service.createExamForm(6);
        QuestionData q = form.getQuestions().getFirst();

        // Assert
        assertThat(form.getQuestions()).hasSize(6);
        assertThat(q.getType()).isEmpty();
        assertThat(q.getPoints()).isZero();
    }

    @Test
    void fillExamForm_success_freeResponse() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "F1",
                2,
                EXAM_ID,
                QuestionTypeDTO.FREE_RESPONSE);

        when(examFacadeService.getExam(EXAM_ID)).thenReturn(exam);
        when(examFacadeService.getQuestionsForExam(EXAM_ID)).thenReturn(List.of(question));

        // Act
        ExamForm form = service.fillExamForm(EXAM_ID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("FREE_RESPONSE");
    }

    @Test
    void fillExamForm_success_mc() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "F1",
                2,
                EXAM_ID,
                QuestionTypeDTO.MC);

        when(examFacadeService.getExam(EXAM_ID)).thenReturn(exam);
        when(examFacadeService.getQuestionsForExam(EXAM_ID)).thenReturn(List.of(question));
        when(examFacadeService.getChoicesForQuestion(question.id())).thenReturn("A, B\nC\nD");

        // Act
        ExamForm form = service.fillExamForm(EXAM_ID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("MC");
    }

    @Test
    void createQuestions_success() {
        // Arrange
        QuestionData q1 = new QuestionData();
        q1.setText("F1");
        q1.setType("FREE_RESPONSE");
        q1.setPoints(1.0);

        QuestionData q2 = new QuestionData();
        q2.setText("F2");
        q2.setType("MC");
        q2.setPoints(2.0);
        q2.setChoices("A\nB");
        q2.setCorrectAnswers("A");

        QuestionData q3 = new QuestionData();
        q3.setText("F3");
        q3.setType("SC");
        q3.setPoints(1.0);
        q3.setChoices("A\nB");
        q3.setCorrectAnswer("A");

        ExamForm form = new ExamForm();
        form.setQuestions(List.of(q1, q2, q3));

        // Act
        service.createQuestions(form, EXAM_ID);

        // Assert
        verify(examFacadeService).createQuestion(argThat(f -> f.text().equals("F1")));
        verify(examFacadeService, times(2)).createChoiceQuestion(any(), any(), any());
    }

    @Test
    void createQuestions_fail_typeNotFound() {
        // Arrange
        QuestionData q1 = new QuestionData();
        q1.setText("F1");
        q1.setType("OTHER_TYPE");
        q1.setPoints(1.0);

        ExamForm form = new ExamForm();
        form.setQuestions(List.of(q1));

        // Act
        assertThrows(IllegalArgumentException.class, () -> service.createQuestions(form, EXAM_ID));

        // Assert
        verify(examFacadeService, never()).createQuestion(any());
    }

    @Test
    void getReviewCoverage_success() {
        when(examFacadeService.reviewCoverage(EXAM_ID)).thenReturn(50.0);

        List<ReviewCoverageForm> result = service.getReviewCoverage(List.of(exam));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getExam()).isEqualTo(exam);
        assertThat(result.getFirst().getReviewCoverage()).isEqualTo(50.0);
    }

    @Test
    void getExamTimeInfo_fail() {
        when(helperService.getExamAvailabilityNotice(exam)).thenReturn("Message");

        ExamTimeInfo info = service.getExamTimeInfo(exam);

        assertThat(info.timeLeft()).isFalse();
    }

    @Test
    void getExamTimeInfo_success() {
        when(helperService.getExamAvailabilityNotice(exam)).thenReturn("");
        when(helperService.getTimeDifference(exam)).thenReturn("Anzeige");

        ExamTimeInfo info = service.getExamTimeInfo(exam);

        assertThat(info.timeLeft()).isTrue();
    }

    @Test
    void getSubmitInfo_aliceHasReview_bobHasNoReview() {
        // Arrange
        StudentDTO student1 = new StudentDTO(UUID.randomUUID(),"Alice");
        StudentDTO student2 = new StudentDTO(UUID.randomUUID(),"Bob");

        when(examFacadeService.getStudentSubmittedExam(EXAM_ID))
                .thenReturn(List.of(student1, student2));

        when(examFacadeService.isSubmitBeingReviewed(EXAM_ID, student1.id())).thenReturn(true);
        when(examFacadeService.isSubmitBeingReviewed(EXAM_ID, student2.id())).thenReturn(false);

        // Act
        List<SubmitInfo> result = service.getSubmitInfo(EXAM_ID);

        // Assert
        assertEquals(2, result.size());

        SubmitInfo info1 = result.getFirst();
        assertEquals("Alice", info1.name());
        assertEquals(student1.id(), info1.id());
        assertTrue(info1.reviewStatus());

        SubmitInfo info2 = result.get(1);
        assertEquals("Bob", info2.name());
        assertEquals(student2.id(), info2.id());
        assertFalse(info2.reviewStatus());

        verify(examFacadeService).getStudentSubmittedExam(EXAM_ID);
        verify(examFacadeService).isSubmitBeingReviewed(EXAM_ID, student1.id());
        verify(examFacadeService).isSubmitBeingReviewed(EXAM_ID, student2.id());
    }

    @ParameterizedTest(name = "{index} => reviewPoints={1}, points={2}, expectedProgress={3}")
    @CsvSource({
            "15, 20, 8.33",  // > 50%
            "10, 20, 8.33",  // = 50%
            "8, 20, 0.0"      // < 50%
    })
    void getEligibilityProgress(int accumulatedPoints, int totalPoints, double expectedProgress) {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);
        List<ExamDTO> exams = List.of(
                new ExamDTO(
                        UUID.randomUUID(),
                        "Exam",
                        UUID.randomUUID(),
                        start,
                        start.plusHours(1),
                        start.plusHours(2))
        );

        AttemptDTO attempt = new AttemptDTO(
                start.plusHours(3),
                accumulatedPoints,
                totalPoints,
                ((double) accumulatedPoints / totalPoints) * 100
        );

        when(examFacadeService.getAllExams()).thenReturn(exams);
        when(helperService.getValidAttempts("student")).thenReturn(List.of(attempt));

        // Act
        double result = service.getEligibilityProgress("student");

        // Assert
        assertThat(result).isCloseTo(expectedProgress, Offset.offset(0.01));
    }

    @ParameterizedTest(name = "{index} => reviewPoints={1}, points={2}, status={3}")
    @CsvSource({
            "15, 20, false",  // > 50%
            "10, 20, false",  // = 50%
            "8, 20, true"     // < 50%
    })
    void failedYetOrNot(int accumulatedPoints, int totalPoints, boolean status) {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);
        List<ExamDTO> exams = List.of(
                new ExamDTO(
                        UUID.randomUUID(),
                        "Exam",
                        UUID.randomUUID(),
                        start,
                        start.plusHours(1),
                        start.plusHours(2))
        );

        AttemptDTO attempt = new AttemptDTO(
                start.plusHours(3),
                accumulatedPoints,
                totalPoints,
                ((double) accumulatedPoints / totalPoints) * 100
        );

        when(examFacadeService.getAllExams()).thenReturn(exams);
        when(helperService.getValidAttempts("student")).thenReturn(List.of(attempt));

        // Act
        boolean result = service.hasAnyFailedAttempt("student");

        // Assert
        assertThat(result).isEqualTo(status);
    }

    @Test
    void createAnswerForm_success() {
        UUID studentUUID = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);

        QuestionDTO question1 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                2,
                EXAM_ID,
                QuestionTypeDTO.FREE_RESPONSE);

        QuestionDTO question2 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 2",
                1,
                EXAM_ID,
                QuestionTypeDTO.FREE_RESPONSE);

        AnswerDTO answer1 = new AnswerDTO(
                UUID.randomUUID(),
                "Answer 1",
                question1.id(),
                studentUUID,
                time
        );

        AnswerDTO answer2 = new AnswerDTO(
                UUID.randomUUID(),
                "Answer 2",
                question2.id(),
                studentUUID,
                time
        );

        Map<QuestionDTO, AnswerDTO> map = new LinkedHashMap<>();
        map.put(question1, answer1);
        map.put(question2, answer2);

        when(examFacadeService.answerHasReview(answer1)).thenReturn(false);
        when(examFacadeService.answerHasReview(answer2)).thenReturn(true);

        List<AnswerForm> result = service.createAnswerForm(map);
        AnswerForm form = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(form.getQuestionText()).isEqualTo("Question 1");
        assertThat(form.getAnswer()).isEqualTo("Answer 1");
        assertThat(form.getQuestionPoints()).isEqualTo(2);
        assertThat(form.getAnswerId()).isEqualTo(answer1.id());
    }

    @Test
    void getFreeResponseAnswersForExamAndStudent() {
        UUID studentId = UUID.randomUUID();

        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                2,
                EXAM_ID,
                QuestionTypeDTO.FREE_RESPONSE
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                question.id(),
                studentId,
                null
        );

        when(examFacadeService.getFreeResponseQuestions(EXAM_ID)).thenReturn(List.of(question));
        when(examFacadeService.getFreeResponseSolutionForExam(EXAM_ID)).thenReturn(List.of(answer));

        Map<QuestionDTO, AnswerDTO> map = service.getFreeResponseSolutionForExamAndStudent(EXAM_ID, studentId);

        assertThat(map).hasSize(1);
    }

    @Test
    void getFreeResponseSolutionForExamAndStudent_noAnswer() {
        UUID studentId = UUID.randomUUID();

        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                2,
                EXAM_ID,
                QuestionTypeDTO.FREE_RESPONSE
        );

        when(examFacadeService.getFreeResponseQuestions(EXAM_ID)).thenReturn(List.of(question));
        when(examFacadeService.getFreeResponseSolutionForExam(EXAM_ID)).thenReturn(List.of());

        Map<QuestionDTO, AnswerDTO> map = service.getFreeResponseSolutionForExamAndStudent(EXAM_ID, studentId);

        assertThat(map).isEmpty();
    }

    @Test
    void createQuestionTypeList() {
        List<QuestionTypeWeb> result = service.createQuestionTypeList(3, 5, 1);

        assertThat(result).hasSize(9);

        assertThat(Collections.frequency(result, QuestionTypeWeb.MC)).isEqualTo(3);
        assertThat(Collections.frequency(result, QuestionTypeWeb.SC)).isEqualTo(5);
        assertThat(Collections.frequency(result, QuestionTypeWeb.FREE_RESPONSE)).isEqualTo(1);
    }
}
