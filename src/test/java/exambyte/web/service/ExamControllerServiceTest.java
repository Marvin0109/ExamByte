package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamControllerService;
import exambyte.application.service.ExamManagementService;
import exambyte.web.form.*;
import net.bytebuddy.asm.Advice;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamControllerServiceTest {

    private ExamManagementService examManagementService;
    private ExamControllerService service;

    @BeforeEach
    void setUp() {
        examManagementService = mock(ExamManagementService.class);
        service = new ExamControllerServiceImpl(examManagementService);
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
    @DisplayName("Das Ausfüllen des ExamForm ist erfolgreich")
    void fillExamForm_01() {
        // Arrange
        UUID examUUID = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        ExamDTO exam = new ExamDTO(
                examUUID,
                "Exam 1",
                UUID.randomUUID(),
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "F1",
                2,
                UUID.randomUUID(),
                examUUID,
                QuestionTypeDTO.FREITEXT);

        when(examManagementService.getExam(examUUID)).thenReturn(exam);
        when(examManagementService.getFragenForExam(examUUID)).thenReturn(List.of(frage));

        // Act
        ExamForm form = service.fillExamForm(examUUID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("FREITEXT");
    }

    @Test
    @DisplayName("Das Ausfüllen des ExamForm ist erfolgreich (Test auf richtige Regex konvertierung)")
    void fillExamForm_02() {
        // Arrange
        UUID examUUID = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        ExamDTO exam = new ExamDTO(
                examUUID,
                "Exam 1",
                UUID.randomUUID(),
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        FrageDTO frage = new FrageDTO(
                UUID.randomUUID(),
                "F1",
                2,
                UUID.randomUUID(),
                examUUID,
                QuestionTypeDTO.MC);

        when(examManagementService.getExam(examUUID)).thenReturn(exam);
        when(examManagementService.getFragenForExam(examUUID)).thenReturn(List.of(frage));
        when(examManagementService.getChoiceForFrage(frage.fachId())).thenReturn("A, B \n C \n D");

        // Act
        ExamForm form = service.fillExamForm(examUUID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("MC");
        assertThat(form.getQuestions().getFirst().getChoices()).isEqualTo("Aĸ B,C,D");
    }

    @Test
    @DisplayName("Erstellen der Fragen ist erfolgreich")
    void createQuestions_01() {
        // Arrange
        UUID examUUID = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();

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
        service.createQuestions(form, profFachId, examUUID);

        // Assert
        verify(examManagementService).createFrage(argThat(f -> f.frageText().equals("F1")));
        verify(examManagementService, times(2)).createChoiceFrage(any(), any(), any());
    }

    @Test
    @DisplayName("Erstellen der Fragen ist nicht erfolgreich, unbehandelter Fragetyp vorhanden")
    void createQuestions_02() {
        // Arrange
        UUID examUUID = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();

        QuestionData q1 = new QuestionData();
        q1.setQuestionText("F1");
        q1.setType("OTHER_TYPE");
        q1.setPunkte(1);

        ExamForm form = new ExamForm();
        form.setQuestions(List.of(q1));

        // Act
        assertThrows(IllegalArgumentException.class, () -> service.createQuestions(form, profFachId, examUUID));

        // Assert
        verify(examManagementService, never()).createFrage(any());
    }

    @Test
    @DisplayName("Die Korrekturgesamtübersicht für alle Exams wird korrekt ermittelt")
    void getReviewCoverage_01() {
        // Arrange
        UUID examUUID = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        ExamDTO exam = new ExamDTO(
                examUUID,
                "Exam 1",
                profFachId,
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        when(examManagementService.reviewCoverage(examUUID)).thenReturn(50.0);

        // Act
        List<ReviewCoverageForm> result = service.getReviewCoverage(List.of(exam));

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getExam()).isEqualTo(exam);
        assertThat(result.getFirst().getReviewCoverage()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Anzeige: Test noch nicht online zum bearbeiten")
    void getExamTimeInfo_01() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        ExamDTO exam = new ExamDTO(
                null,
                "Exam",
                null,
                start,
                end,
                end.plusHours(2)
        );

        // Act
        ExamTimeInfo info = service.getExamTimeInfo(exam);

        // Assert
        assertThat(info.timeLeft()).isFalse();
        assertThat(info.fristAnzeige()).contains("Der Test kann erst ab den");
        assertThat(info.fristAnzeige()).contains(start.getDayOfMonth() + "");
    }

    @Test
    @DisplayName("Anzeige: Test schon beendet worden")
    void getExamTimeInfo_02() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusHours(3);
        LocalDateTime end = LocalDateTime.now().minusHours(1);

        ExamDTO exam = new ExamDTO(
                null,
                "Exam",
                null,
                start,
                end,
                end.plusHours(1)
        );

        // Act
        ExamTimeInfo info = service.getExamTimeInfo(exam);

        // Assert
        assertThat(info.timeLeft()).isFalse();
        assertThat(info.fristAnzeige()).contains("überschritten");
        assertThat(info.fristAnzeige()).contains(end.getHour() + "");
    }

    @Test
    @DisplayName("Anzeige: Test online, Restzeit wird angezeigt")
    void getExamTimeInfo_03() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusMinutes(10);
        LocalDateTime end = LocalDateTime.now().plusMinutes(50);

        ExamDTO exam = new ExamDTO(
                null,
                "Exam",
                null,
                start,
                end,
                end.plusHours(1)
        );

        // Act
        ExamTimeInfo info = service.getExamTimeInfo(exam);

        // Assert
        assertThat(info.timeLeft()).isTrue();
        assertThat(info.fristAnzeige()).contains("Minute");
    }

    @Test
    @DisplayName("Bewertungsstatus: Alice hat Bewertung, Bob nicht")
    void getSubmitInfo_01() {
        // Arrange
        UUID examUUID = UUID.randomUUID();

        StudentDTO student1 = new StudentDTO(UUID.randomUUID(),"Alice");
        StudentDTO student2 = new StudentDTO(UUID.randomUUID(),"Bob");

        when(examManagementService.getStudentSubmittedExam(examUUID))
                .thenReturn(List.of(student1, student2));

        when(examManagementService.isSubmitBeingReviewed(examUUID, student1)).thenReturn(true);
        when(examManagementService.isSubmitBeingReviewed(examUUID, student2)).thenReturn(false);

        // Act
        List<SubmitInfo> result = service.getSubmitInfo(examUUID);

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

        verify(examManagementService).getStudentSubmittedExam(examUUID);
        verify(examManagementService).isSubmitBeingReviewed(examUUID, student1);
        verify(examManagementService).isSubmitBeingReviewed(examUUID, student2);
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

        when(examManagementService.getAllExams()).thenReturn(exams);
        when(examManagementService.getSubmission(exams.getFirst().fachId(), "student")).thenReturn(versuch);

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

        when(examManagementService.getAllExams()).thenReturn(exams);
        when(examManagementService.getSubmission(exams.getFirst().fachId(), "student")).thenReturn(versuch);

        // Act
        boolean result = service.hasAnyFailedAttempt("student");

        // Assert
        assertThat(result).isEqualTo(status);
    }

    @Test
    void createAnswerForm_success() {
        UUID studentUUID = UUID.randomUUID();
        UUID examUUID = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);

        FrageDTO frage1 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                2,
                UUID.randomUUID(),
                examUUID,
                QuestionTypeDTO.FREITEXT);

        FrageDTO frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                1,
                UUID.randomUUID(),
                examUUID,
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

        when(examManagementService.antwortHasReview(antwort1)).thenReturn(false);
        when(examManagementService.antwortHasReview(antwort2)).thenReturn(true);

        List<AnswerForm> result = service.createAnswerForm(map);
        AnswerForm form = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(form.getFrageText()).isEqualTo("Frage 1");
        assertThat(form.getAntwort()).isEqualTo("Antwort 1");
        assertThat(form.getMaxPunkte()).isEqualTo(2);
        assertThat(form.getAntwortFachId()).isEqualTo(antwort1.fachId());
    }
}
