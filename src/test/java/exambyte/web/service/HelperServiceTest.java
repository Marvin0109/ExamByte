package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamFacadeService;
import exambyte.web.form.load_old_submit_data.OldDataDTO;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_exam.ExamAggregateDTO;
import exambyte.web.form.show_exam.ExamViewForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HelperServiceTest {

    private HelperService helperService;
    private static final UUID QUESTION_ID = UUID.randomUUID();

    @Mock
    private ExamFacadeService examFacadeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC")
        );

        helperService = new HelperServiceImpl(examFacadeService, fixedClock);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("examWithDiffResultTime")
    void getValidAttempts(
            String description,
            LocalDateTime start,
            LocalDateTime end,
            LocalDateTime result,
            int size
    ) {
        ExamDTO examDTO = new ExamDTO(
                UUID.randomUUID(),
                "Title",
                null,
                start,
                end,
                result
        );

        AttemptDTO attemptDTO = new AttemptDTO(
                null,
                1.0,
                1.0,
                100.0
        );

        when(examFacadeService.getAllExams()).thenReturn(List.of(examDTO));
        when(examFacadeService.getSubmission(any(), any())).thenReturn(attemptDTO);

        List<AttemptDTO> validAttempts = helperService.getValidAttempts("StudentName");

        assertThat(validAttempts).hasSize(size);
    }

    static Stream<Arguments> examWithDiffResultTime() {
        return Stream.of(
                Arguments.of(
                        "One attempt found",
                        LocalDateTime.of(2026, 1, 1, 0,0),
                        LocalDateTime.of(2026, 1, 1, 2, 0),
                        LocalDateTime.of(2026, 1, 1, 3, 0),
                        1
                ),

                Arguments.of(
                        "No attempt found",
                        LocalDateTime.of(2026, 1, 1, 0,0),
                        LocalDateTime.of(2026, 1, 1, 12, 0),
                        LocalDateTime.of(2026, 1, 1, 13, 0),
                        0
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("examDiffTimeInput")
    void getExamAvailabilityNotice(
            String description,
            LocalDateTime start,
            LocalDateTime end,
            String message
    ) {
        ExamDTO examDTO = new ExamDTO(
                null,
                "Title",
                null,
                start,
                end,
                start.plusDays(1)
        );

        String result = helperService.getExamAvailabilityNotice(examDTO);

        assertThat(result).contains(message);
    }

    static Stream<Arguments> examDiffTimeInput() {
        return Stream.of(
                Arguments.of(
                        "Exam is over (1)",
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        LocalDateTime.of(2026, 1, 1, 1, 0),
                        "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten."
                ),

                Arguments.of(
                        "Exam is over (2)",
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten."
                ),

                Arguments.of(
                        "Exam is about to start",
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        LocalDateTime.of(2026, 1, 1, 12, 0),
                        "Der Test kann erst ab den "
                ),

                Arguments.of(
                        "Exam is running",
                        LocalDateTime.of(2026, 1, 1, 9, 0),
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        ""
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("inputForTimeDiff")
    void getTimeDifference(
            String description,
            LocalDateTime end,
            String message
    ) {
        ExamDTO examDTO = new ExamDTO(
                null,
                "Title",
                null,
                LocalDateTime.of(2026, 1, 1, 0, 0),
                end,
                LocalDateTime.of(2027, 1, 1, 0, 0)
        );

        String result = helperService.getTimeDifference(examDTO);

        assertThat(result).contains(message);
    }

    static Stream<Arguments> inputForTimeDiff() {
        return Stream.of(
                Arguments.of(
                        "Displaying minutes",
                        LocalDateTime.of(2026, 1, 1, 10, 1),
                        "1 Minute"
                ),

                Arguments.of(
                        "Displaying hours",
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        "1 Stunde"
                ),

                Arguments.of(
                        "Displaying days",
                        LocalDateTime.of(2026, 1, 2, 10, 0),
                        "1 Tag"
                ),

                Arguments.of(
                        "Full display (1)",
                        LocalDateTime.of(2026, 1, 14, 12, 5),
                        "13 Tage 2 Stunden 5 Minuten"
                ),

                // Difference between: 2026-01-01:10-00-00 and 2026-01-12:01-01-34 are 10 days, 15 hours and 34 min
                Arguments.of(
                        "Full display (2)",
                        LocalDateTime.of(2026, 1, 12, 1, 34),
                        "10 Tage 15 Stunden 34 Minuten"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("inputForSplitLogic")
    void normalizeAnswerForFrontend(
            String toSplit,
            String result
    ) {
        String ans = helperService.normalizeAnswerForFrontend(toSplit);

        assertThat(ans).isEqualTo(result);
    }

    static Stream<Arguments> inputForSplitLogic() {
        return Stream.of(
                Arguments.of(
                        "a,b d,,,",
                        "aĸb dĸĸĸ"
                ),

                Arguments.of(
                        "a\nc, ds, a\n, d",
                        "a,cĸ dsĸ a,ĸ d"
                ),

                Arguments.of(
                        "a\nb\nc\n,d,\naf,fa!",
                        "a,b,c,ĸdĸ,afĸfa!"
                )
        );
    }

    @Test
    void prepareFrageData_freeResponse() {
        QuestionDTO questionDTO = new QuestionDTO(
                QUESTION_ID,
                "Question",
                2,
                UUID.randomUUID(),
                QuestionTypeDTO.FREE_RESPONSE
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                questionDTO.id(),
                UUID.randomUUID(),
                null
        );

        when(examFacadeService.getAnswerForQuestionIdAndStudentId(eq(questionDTO.id()), any())).thenReturn(answer);
        when(examFacadeService.getCorrectAnswerForQuestion(questionDTO.id())).thenReturn(null);

        PreparedQuestionData result = helperService.prepareFrageData(questionDTO, UUID.randomUUID());

        assertThat(result.correctAnswers()).isNull();
        assertThat(result.question()).isEqualTo(questionDTO);
        assertThat(result.answer()).isEqualTo(answer);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("differentQuestionType")
    void prepareQuestionData_choiceQuestions(
            QuestionTypeDTO type
    ) {
        QuestionDTO questionDTO = new QuestionDTO(
                QUESTION_ID,
                "Question",
                2,
                UUID.randomUUID(),
                type
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                questionDTO.id(),
                UUID.randomUUID(),
                null
        );

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                questionDTO.id()
        );

        when(examFacadeService.getAnswerForQuestionIdAndStudentId(eq(questionDTO.id()), any())).thenReturn(answer);
        when(examFacadeService.getCorrectAnswerForQuestion(questionDTO.id())).thenReturn(correctAnswers);

        PreparedQuestionData result = helperService.prepareFrageData(questionDTO, UUID.randomUUID());

        assertThat(result.correctAnswers()).isNotNull();
        assertThat(result.question()).isEqualTo(questionDTO);
        assertThat(result.answer()).isNotNull();
    }

    static Stream<Arguments> differentQuestionType() {
        return Stream.of(
                Arguments.of(
                        QuestionTypeDTO.MC
                ),

                Arguments.of(
                        QuestionTypeDTO.SC
                )
        );
    }

    @Test
    void prepareQuestionData_answerNull() {
        QuestionDTO questionDTO = new QuestionDTO(
                QUESTION_ID,
                "Question",
                2,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                questionDTO.id()
        );

        when(examFacadeService.getAnswerForQuestionIdAndStudentId(eq(questionDTO.id()), any())).thenReturn(null);
        when(examFacadeService.getCorrectAnswerForQuestion(questionDTO.id())).thenReturn(correctAnswers);

        PreparedQuestionData result = helperService.prepareFrageData(questionDTO, UUID.randomUUID());

        assertThat(result.correctAnswers()).isNotNull();
        assertThat(result.question()).isEqualTo(questionDTO);
        assertThat(result.answer()).isNull();
    }

    @Test
    void prepareReviewViewForm() {
        // Arrange
        UUID examId = UUID.randomUUID();

        ExamDTO exam = mock(ExamDTO.class);
        when(exam.title()).thenReturn("Title");
        when(examFacadeService.getExam(any())).thenReturn(exam);

        UUID studentId = UUID.randomUUID();
        when(examFacadeService.getStudentIdByName("Student")).thenReturn(studentId);

        AttemptDTO attempt = mock(AttemptDTO.class);
        when(attempt.accumulatedPoints()).thenReturn(10.0);
        when(attempt.totalPoints()).thenReturn(20.0);
        when(examFacadeService.getSubmission(examId, "Student")).thenReturn(attempt);

        QuestionDTO question = new QuestionDTO(
                QUESTION_ID,
                "Question",
                20,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );
        when(examFacadeService.getQuestionsForExam(examId)).thenReturn(List.of(question));

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                question.id(),
                studentId,
                null
        );
        when(examFacadeService.getAnswerForQuestionIdAndStudentId(question.id(), studentId)).thenReturn(answer);

        CorrectAnswersDTO solution = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                question.id()
        );
        when(examFacadeService.getCorrectAnswerForQuestion(question.id())).thenReturn(solution);

        ReviewerDTO reviewer = new ReviewerDTO(UUID.randomUUID(), "Auto reviewer");
        when(examFacadeService.getReviewerById(reviewer.id())).thenReturn(reviewer);

        ReviewDTO reviewDTO = new ReviewDTO(
                UUID.randomUUID(),
                answer.id(),
                reviewer.id(),
                "Lösung: A; B",
                10
        );
        when(examFacadeService.getReviewForAnswer(answer.id())).thenReturn(reviewDTO);

        // Act
        ReviewViewForm result = helperService.prepareReviewViewForm(examId, "Student");

        // Assert
        assertThat(result.authorName()).isEmpty();
        assertThat(result.examTitle()).isEqualTo("Title");
        assertThat(result.reviewPoints()).isEqualTo(10.0);
        assertThat(result.maxPoints()).isEqualTo(20.0);
        assertThat(result.components()).hasSize(1);
    }

    @Test
    void prepareReviewViewForm_reviewNull() {
        // Arrange
        UUID examId = UUID.randomUUID();

        ExamDTO exam = mock(ExamDTO.class);
        when(exam.title()).thenReturn("Title");
        when(examFacadeService.getExam(any())).thenReturn(exam);

        UUID studentId = UUID.randomUUID();
        when(examFacadeService.getStudentIdByName("Student")).thenReturn(studentId);

        AttemptDTO attempt = mock(AttemptDTO.class);
        when(attempt.accumulatedPoints()).thenReturn(0.0);
        when(attempt.totalPoints()).thenReturn(20.0);
        when(examFacadeService.getSubmission(examId, "Student")).thenReturn(attempt);

        QuestionDTO question = new QuestionDTO(
                QUESTION_ID,
                "Question",
                20,
                UUID.randomUUID(),
                QuestionTypeDTO.FREE_RESPONSE
        );
        when(examFacadeService.getQuestionsForExam(examId)).thenReturn(List.of(question));

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                question.id(),
                studentId,
                null
        );
        when(examFacadeService.getAnswerForQuestionIdAndStudentId(question.id(), studentId)).thenReturn(answer);

        CorrectAnswersDTO k = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                question.id()
        );
        when(examFacadeService.getCorrectAnswerForQuestion(question.id())).thenReturn(k);

        when(examFacadeService.getReviewForAnswer(answer.id())).thenReturn(null);

        // Act
        ReviewViewForm result = helperService.prepareReviewViewForm(examId, "Student");

        // Assert
        assertThat(result.authorName()).isEmpty();
        assertThat(result.examTitle()).isEqualTo("Title");
        assertThat(result.reviewPoints()).isEqualTo(0.0);
        assertThat(result.maxPoints()).isEqualTo(20.0);
        assertThat(result.components()).hasSize(1);
    }

    @Test
    void prepareReviewViewForm_answerNull() {
        // Arrange
        UUID examId = UUID.randomUUID();

        ExamDTO exam = mock(ExamDTO.class);
        when(exam.title()).thenReturn("Title");
        when(examFacadeService.getExam(any())).thenReturn(exam);

        UUID studentId = UUID.randomUUID();
        when(examFacadeService.getStudentIdByName("Student")).thenReturn(studentId);

        AttemptDTO attempt = mock(AttemptDTO.class);
        when(attempt.accumulatedPoints()).thenReturn(0.0);
        when(attempt.totalPoints()).thenReturn(20.0);
        when(examFacadeService.getSubmission(examId, "Student")).thenReturn(attempt);

        QuestionDTO question = new QuestionDTO(
                QUESTION_ID,
                "Question",
                20,
                UUID.randomUUID(),
                QuestionTypeDTO.FREE_RESPONSE
        );
        when(examFacadeService.getQuestionsForExam(examId)).thenReturn(List.of(question));

        when(examFacadeService.getAnswerForQuestionIdAndStudentId(question.id(), studentId)).thenReturn(null);

        when(examFacadeService.getCorrectAnswerForQuestion(question.id())).thenReturn(null);

        // Act
        ReviewViewForm result = helperService.prepareReviewViewForm(examId, "Student");

        // Assert
        assertThat(result.authorName()).isEmpty();
        assertThat(result.examTitle()).isEqualTo("Title");
        assertThat(result.reviewPoints()).isEqualTo(0.0);
        assertThat(result.maxPoints()).isEqualTo(20.0);
        assertThat(result.components()).hasSize(1);

        var element = result.components().getFirst();
        assertThat(element.review().text()).isEqualTo("Keine Bewertung");
        assertThat(element.answer().answer()).isEmpty();
    }

    @Test
    void fillOldDataForm_mapsQuestionToOldDataForm() {
        // Arrange
        UUID examId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        ExamDTO exam = new ExamDTO(
                examId,
                "Mathe",
                UUID.randomUUID(),
                null,
                null,
                null);

        QuestionDTO question = new QuestionDTO(
                QUESTION_ID,
                "Question",
                1,
                examId,
                QuestionTypeDTO.MC
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                question.id(),
                studentId,
                null
        );

        CorrectAnswersDTO solution = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "Answer",
                "Answer\nAnswer 2",
                question.id()
        );

        when(examFacadeService.getExam(examId)).thenReturn(exam);
        when(examFacadeService.getStudentIdByName("Max")).thenReturn(studentId);
        when(examFacadeService.getQuestionsForExam(examId)).thenReturn(List.of(question));

        when(examFacadeService.getAnswerForQuestionIdAndStudentId(question.id(), studentId)).thenReturn(answer);
        when(examFacadeService.getCorrectAnswerForQuestion(question.id())).thenReturn(solution);

        // Act
        OldDataForm result = helperService.fillOldDataForm(examId, "Max");

        // Assert
        assertThat(result.examId()).isEqualTo(examId);
        assertThat(result.examTitle()).isEqualTo("Mathe");
        assertThat(result.oldDataDTOs()).hasSize(1);

        OldDataDTO dto = result.oldDataDTOs().getFirst();
        assertThat(dto.question()).isEqualTo(question);
        assertThat(dto.answer()).isNotNull();
        assertThat(dto.correctAnswers()).isNotNull();
    }

    @Test
    void fillSubmitForm_mcWithAnswer_splitsAndTrims() {
        OldDataDTO dto = new OldDataDTO(
                new QuestionDTO(
                        UUID.randomUUID(),
                        "Question",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.MC
                ),
                null,
                new AnswerDTO(
                        UUID.randomUUID(),
                        "A, B , C",
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null
                )
        );

        OldDataForm form = new OldDataForm(
                UUID.randomUUID(),
                "Exam",
                List.of(dto)
        );

        SubmitForm result = helperService.fillSubmitFormWithData(form);

        assertThat(result.getAnswers())
                .containsEntry(
                        dto.question().id().toString(),
                        List.of("A", "B", "C")
                );
    }

    @Test
    void fillSubmitForm_mcWithoutAnswer_returnsEmptyList() {
        OldDataDTO dto = new OldDataDTO(
                new QuestionDTO(
                        UUID.randomUUID(),
                        "Question",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.MC
                ),
                null,
                null
        );

        OldDataForm form = new OldDataForm(
                UUID.randomUUID(),
                "Exam",
                List.of(dto)
        );

        SubmitForm result = helperService.fillSubmitFormWithData(form);

        assertThat(result.getAnswers())
                .containsEntry(
                        dto.question().id().toString(),
                        List.of()
                );
    }

    @Test
    void fillSubmitForm_freeResponseAnswer() {
        OldDataDTO dto = new OldDataDTO(
                new QuestionDTO(
                        UUID.randomUUID(),
                        "Question",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.FREE_RESPONSE
                ),
                null,
                new AnswerDTO(
                        UUID.randomUUID(),
                        "Answer",
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null
                )
        );

        OldDataForm form = new OldDataForm(
                UUID.randomUUID(),
                "Exam",
                List.of(dto)
        );

        SubmitForm result = helperService.fillSubmitFormWithData(form);

        assertThat(result.getAnswers())
                .containsEntry(
                        dto.question().id().toString(),
                        List.of("Answer")
                );
    }

    @Test
    void fillSubmitForm_freeResponseWithoutAnswer() {
        OldDataDTO dto = new OldDataDTO(
                new QuestionDTO(
                        UUID.randomUUID(),
                        "Question",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.FREE_RESPONSE
                ),
                null,
                null
        );

        OldDataForm form = new OldDataForm(
                UUID.randomUUID(),
                "Exam",
                List.of(dto)
        );

        SubmitForm result = helperService.fillSubmitFormWithData(form);

        assertThat(result.getAnswers())
                .containsEntry(
                        dto.question().id().toString(),
                        List.of("")
                );
    }

    @Test
    void prepareExamViewForm() {
        // Arrange
        ExamDTO mockExam = mock(ExamDTO.class);
        when(mockExam.professorId()).thenReturn(UUID.randomUUID());
        when(mockExam.title()).thenReturn("Exam");

        when(examFacadeService.getExam(any())).thenReturn(mockExam);

        ProfessorDTO mockProf = mock(ProfessorDTO.class);
        when(mockProf.name()).thenReturn("Professor");

        when(examFacadeService.getProfessor(any())).thenReturn(mockProf);

        UUID question1Id = UUID.randomUUID();
        UUID question2Id = UUID.randomUUID();

        List<QuestionDTO> questions = List.of(
                new QuestionDTO(
                        question1Id,
                        "Question 1",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.SC
                ),

                new QuestionDTO(
                        question2Id,
                        "Question 2",
                        4,
                        UUID.randomUUID(),
                        QuestionTypeDTO.FREE_RESPONSE
                )
        );

        CorrectAnswersDTO k = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A",
                "A\nB\nC",
                question1Id
        );

        when(examFacadeService.getQuestionsForExam(any())).thenReturn(questions);

        when(examFacadeService.getCorrectAnswerForQuestion(question1Id)).thenReturn(k);
        when(examFacadeService.getCorrectAnswerForQuestion(question2Id)).thenReturn(null);

        // Act
        ExamViewForm result = helperService.prepareExamViewForm(UUID.randomUUID());
        List<ExamAggregateDTO> resultComponents = result.questions();

        // Assert
        assertThat(result.examTitle()).isEqualTo("Exam");
        assertThat(result.authorName()).isEqualTo("Professor");
        assertThat(result.points()).isEqualTo(5 );

        assertThat(resultComponents).hasSize(2);
        assertThat(resultComponents.getFirst().correctAnswers()).isNotNull();
        assertThat(resultComponents.getLast().correctAnswers()).isNull();
    }
}
