package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamControllerService;
import exambyte.application.service.ExamFacadeService;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.create_exam.QuestionData;
import exambyte.web.form.info.ReviewCoverageForm;
import exambyte.web.form.show_review.ReviewAggregateDTO;
import exambyte.web.form.show_review.ReviewViewForm;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @DisplayName("ExamForm wird erfolgreich erstellt")
    void createExamForm_01() {
        // Act
        ExamForm form = service.createExamForm();
        QuestionData q = form.getQuestions().getFirst();

        // Assert
        assertThat(form.getQuestions()).hasSize(6);
        assertThat(q.getType()).isEmpty();
        assertThat(q.getPunkte()).isZero();
    }

    @Test
    @DisplayName("Das Ausfüllen des ExamForm ist erfolgreich (Eine Freitext Aufgabe)")
    void fillExamForm_01() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "F1",
                2,
                UUID.randomUUID(),
                EXAM_ID,
                QuestionTypeDTO.FREITEXT);

        when(examFacadeService.getExam(EXAM_ID)).thenReturn(exam);
        when(examFacadeService.getFragenForExam(EXAM_ID)).thenReturn(List.of(frage));

        // Act
        ExamForm form = service.fillExamForm(EXAM_ID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("FREITEXT");
    }

    @Test
    @DisplayName("Das Ausfüllen des ExamForm ist erfolgreich (MC Aufgabe)")
    void fillExamForm_02() {
        // Arrange
        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "F1",
                2,
                UUID.randomUUID(),
                EXAM_ID,
                QuestionTypeDTO.MC);

        when(examFacadeService.getExam(EXAM_ID)).thenReturn(exam);
        when(examFacadeService.getFragenForExam(EXAM_ID)).thenReturn(List.of(frage));
        when(examFacadeService.getChoiceForFrage(frage.fachId())).thenReturn("A, B\nC\nD");
        //when(helperService.split(any())).thenReturn(List.of("Aĸ B", "C", "D"));

        // Act
        ExamForm form = service.fillExamForm(EXAM_ID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("MC");
    }

    @Test
    @DisplayName("Erstellen der Fragen ist erfolgreich")
    void createQuestions_01() {
        // Arrange
        QuestionData q1 = new QuestionData();
        q1.setQuestionText("F1");
        q1.setType("FREITEXT");
        q1.setPunkte(1);

        QuestionData q2 = new QuestionData();
        q2.setQuestionText("F2");
        q2.setType("MC");
        q2.setPunkte(2);
        q2.setChoices("A\nB");
        q2.setCorrectAnswers("A");

        QuestionData q3 = new QuestionData();
        q3.setQuestionText("F3");
        q3.setType("SC");
        q3.setPunkte(1);
        q3.setChoices("A\nB");
        q3.setCorrectAnswer("A");

        ExamForm form = new ExamForm();
        form.setQuestions(List.of(q1, q2, q3));

        // Act
        service.createQuestions(form, PROF_ID, EXAM_ID);

        // Assert
        verify(examFacadeService).createFrage(argThat(f -> f.frageText().equals("F1")));
        verify(examFacadeService, times(2)).createChoiceFrage(any(), any(), any());
    }

    @Test
    @DisplayName("Erstellen der Fragen ist nicht erfolgreich, unbehandelter Fragetyp vorhanden")
    void createQuestions_02() {
        // Arrange
        QuestionData q1 = new QuestionData();
        q1.setQuestionText("F1");
        q1.setType("OTHER_TYPE");
        q1.setPunkte(1);

        ExamForm form = new ExamForm();
        form.setQuestions(List.of(q1));

        // Act
        assertThrows(IllegalArgumentException.class, () -> service.createQuestions(form, PROF_ID, EXAM_ID));

        // Assert
        verify(examFacadeService, never()).createFrage(any());
    }

    @Test
    @DisplayName("Die Korrekturgesamtübersicht für alle Exams wird korrekt ermittelt")
    void getReviewCoverage_01() {
        when(examFacadeService.reviewCoverage(EXAM_ID)).thenReturn(50.0);

        List<ReviewCoverageForm> result = service.getReviewCoverage(List.of(exam));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getExam()).isEqualTo(exam);
        assertThat(result.getFirst().getReviewCoverage()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Test nicht verfügbar")
    void getExamTimeInfo_01() {
        when(helperService.getExamAvailabilityNotice(exam)).thenReturn("Message");

        ExamTimeInfo info = service.getExamTimeInfo(exam);

        assertThat(info.timeLeft()).isFalse();
    }

    @Test
    @DisplayName("Test verfügbar")
    void getExamTimeInfo_02() {
        when(helperService.getExamAvailabilityNotice(exam)).thenReturn("");
        when(helperService.getTimeDifference(exam)).thenReturn("Anzeige");

        ExamTimeInfo info = service.getExamTimeInfo(exam);

        assertThat(info.timeLeft()).isTrue();
    }

    @Test
    @DisplayName("Bewertungsstatus: Alice hat Bewertung, Bob nicht")
    void getSubmitInfo_01() {
        // Arrange
        StudentDTO student1 = new StudentDTO(UUID.randomUUID(),"Alice");
        StudentDTO student2 = new StudentDTO(UUID.randomUUID(),"Bob");

        when(examFacadeService.getStudentSubmittedExam(EXAM_ID))
                .thenReturn(List.of(student1, student2));

        when(examFacadeService.isSubmitBeingReviewed(EXAM_ID, student1.fachId())).thenReturn(true);
        when(examFacadeService.isSubmitBeingReviewed(EXAM_ID, student2.fachId())).thenReturn(false);

        // Act
        List<SubmitInfo> result = service.getSubmitInfo(EXAM_ID);

        // Assert
        assertEquals(2, result.size());

        SubmitInfo info1 = result.getFirst();
        assertEquals("Alice", info1.name());
        assertEquals(student1.fachId(), info1.fachId());
        assertTrue(info1.reviewStatus());

        SubmitInfo info2 = result.get(1);
        assertEquals("Bob", info2.name());
        assertEquals(student2.fachId(), info2.fachId());
        assertFalse(info2.reviewStatus());

        verify(examFacadeService).getStudentSubmittedExam(EXAM_ID);
        verify(examFacadeService).isSubmitBeingReviewed(EXAM_ID, student1.fachId());
        verify(examFacadeService).isSubmitBeingReviewed(EXAM_ID, student2.fachId());
    }

    @ParameterizedTest(name = "{index} => erreichtePunkte={1}, maxPunkte={2}, expectedProgress={3}")
    @CsvSource({
            "15, 20, 8.33",  // > 50%
            "10, 20, 8.33",  // = 50%
            "8, 20, 0.0"      // < 50%
    })
    void getZulassungProgress(int erreichtePunkte, int maxPunkte, double expectedProgress) {
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

        VersuchDTO versuch = new VersuchDTO(
                start.plusHours(3),
                erreichtePunkte,
                maxPunkte,
                ((double) erreichtePunkte / maxPunkte) * 100
        );

        when(examFacadeService.getAllExams()).thenReturn(exams);
        when(helperService.getValidAttempts("student")).thenReturn(List.of(versuch));

        // Act
        double result = service.getZulassungsProgress("student");

        // Assert
        assertThat(result).isCloseTo(expectedProgress, Offset.offset(0.01));
    }

    @ParameterizedTest(name = "{index} => erreichtePunkte={1}, maxPunkte={2}, status={3}")
    @CsvSource({
            "15, 20, false",  // > 50%
            "10, 20, false",  // = 50%
            "8, 20, true"     // < 50%
    })
    void failedYetOrNot(int erreichtePunkte, int maxPunkte, boolean status) {
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

        VersuchDTO versuch = new VersuchDTO(
                start.plusHours(3),
                erreichtePunkte,
                maxPunkte,
                ((double) erreichtePunkte / maxPunkte) * 100
        );

        when(examFacadeService.getAllExams()).thenReturn(exams);
        when(helperService.getValidAttempts("student")).thenReturn(List.of(versuch));

        // Act
        boolean result = service.hasAnyFailedAttempt("student");

        // Assert
        assertThat(result).isEqualTo(status);
    }

    @Test
    void createAnswerForm_success() {
        UUID studentUUID = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);

        FrageDTO frage1 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                2,
                UUID.randomUUID(),
                EXAM_ID,
                QuestionTypeDTO.FREITEXT);

        FrageDTO frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                1,
                UUID.randomUUID(),
                EXAM_ID,
                QuestionTypeDTO.FREITEXT);

        AntwortDTO antwort1 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 1",
                frage1.fachId(),
                studentUUID,
                time
        );

        AntwortDTO antwort2 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 2",
                frage2.fachId(),
                studentUUID,
                time
        );

        Map<FrageDTO, AntwortDTO> map = new LinkedHashMap<>();
        map.put(frage1, antwort1);
        map.put(frage2, antwort2);

        when(examFacadeService.antwortHasReview(antwort1)).thenReturn(false);
        when(examFacadeService.antwortHasReview(antwort2)).thenReturn(true);

        List<AnswerForm> result = service.createAnswerForm(map);
        AnswerForm form = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(form.getFrageText()).isEqualTo("Frage 1");
        assertThat(form.getAntwort()).isEqualTo("Antwort 1");
        assertThat(form.getMaxPunkte()).isEqualTo(2);
        assertThat(form.getAntwortFachId()).isEqualTo(antwort1.fachId());
    }

    @Test
    void prepareReviewViewForm_allFound() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();
        UUID antwortId = UUID.randomUUID();
        UUID korrektorId = UUID.randomUUID();

        when(examFacadeService.getExam(EXAM_ID)).thenReturn(exam);

        when(examFacadeService.getStudentIdByName("Max")).thenReturn(studentId);

        when(examFacadeService.getSubmission(EXAM_ID, "Max")).
                thenReturn(new VersuchDTO(null, 5, 10, 50.0));

        FrageDTO frage = new FrageDTO(
                frageId,
                "Frage",
                10,
                UUID.randomUUID(),
                EXAM_ID,
                QuestionTypeDTO.FREITEXT
        );

        when(examFacadeService.getFragenForExam(EXAM_ID)).thenReturn(List.of(frage));

        AntwortDTO antwort = new AntwortDTO(
                antwortId,
                "A,B",
                frageId,
                studentId,
                LocalDateTime.of(2000, 1, 1, 0, 0)
        );

        PreparedFrageData prepared = new PreparedFrageData(
                frage,
                antwort,
                new KorrekteAntwortenDTO(null, "A", "A,B", frageId)
        );

        when(helperService.prepareFrageData(frage, studentId)).thenReturn(prepared);
        //when(helperService.splitOldDataMC("A,B")).thenReturn(List.of("A", "B"));

        ReviewDTO review = new ReviewDTO(
                null,
                antwortId,
                korrektorId,
                "Bewertung",
                5
        );

        when(examFacadeService.getReviewForAntwort(antwortId)).thenReturn(review);
        when(examFacadeService.getReviewerById(korrektorId)).
                thenReturn(new KorrektorDTO(null, "Korrektor"));

        ReviewViewForm result = service.prepareReviewViewForm(EXAM_ID, "Max");

        assertEquals("Exam", result.examTitle());
        assertEquals("Korrektor", result.authorName());
        assertEquals(5, result.erreichtePunkte());
        assertEquals(10, result.maxPunkte());

        assertEquals(1, result.components().size());

        ReviewAggregateDTO agg = result.components().getFirst();
        assertEquals(frage, agg.frage());
        assertEquals("A,B", agg.antwort().antwortText());
        assertNotNull(agg.review());
    }

    @Test
    void prepareReviewViewForm_reviewMissing() {
        // Arrange
        UUID studentUUID = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamDTO examDTO = new ExamDTO(
                EXAM_ID,
                "Titel",
                UUID.randomUUID(),
                time,
                time.plusHours(1),
                time.plusHours(2)
        );

        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                1,
                UUID.randomUUID(),
                EXAM_ID,
                QuestionTypeDTO.FREITEXT
        );

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frage.fachId(),
                studentUUID,
                LocalDateTime.of(2000, 1, 1, 0, 0)
        );

        KorrekteAntwortenDTO korrekteAntwortenDTO = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort 1",
                "Antwort 1\nAntwort 2",
                frage.fachId()
        );

        VersuchDTO versuch = new VersuchDTO(
                time,
                1.0,
                1.0,
                100.0
        );

        when(examFacadeService.getExam(examDTO.fachId())).thenReturn(examDTO);
        when(examFacadeService.getStudentIdByName(any())).thenReturn(studentUUID);

        when(examFacadeService.getSubmission(examDTO.fachId(), "student")).thenReturn(versuch);

        when(examFacadeService.getFragenForExam(examDTO.fachId())).thenReturn(List.of(frage));

        when(examFacadeService.getAntwortForFrageAndStudent(frage.fachId(), studentUUID)).thenReturn(antwort);

        when(examFacadeService.getReviewForAntwort(antwort.fachId())).thenReturn(null);

        when(examFacadeService.getLoesungForFrage(frage.fachId())).thenReturn(korrekteAntwortenDTO);

        // Act
        ReviewViewForm rvf = service.prepareReviewViewForm(examDTO.fachId(), "student");
        ReviewAggregateDTO agg = rvf.components().getFirst();

        // Assert
        assertNotNull(rvf);
        assertEquals(frage, agg.frage());
        assertEquals(antwort, agg.antwort());
        assertEquals("", rvf.authorName());
        assertNull(agg.review());
        assertEquals(korrekteAntwortenDTO, agg.korrekteAntworten());
    }
}
