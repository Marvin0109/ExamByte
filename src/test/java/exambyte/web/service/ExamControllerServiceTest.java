package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.service.ExamControllerService;
import exambyte.application.service.ExamManagementService;
import exambyte.web.form.ExamForm;
import exambyte.web.form.ExamTimeInfo;
import exambyte.web.form.QuestionData;
import exambyte.web.form.ReviewCoverageForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExamControllerServiceTest {

    private ExamManagementService examService;
    private ExamControllerService service;

    @BeforeEach
    void setUp() {
        examService = mock(ExamManagementService.class);
        service = new ExamControllerServiceImpl(examService);
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
                null,
                examUUID,
                "Exam 1",
                UUID.randomUUID(),
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        FrageDTO frage = new FrageDTO(
                null,
                UUID.randomUUID(),
                "F1",
                2,
                QuestionTypeDTO.FREITEXT,
                UUID.randomUUID(),
                examUUID);

        when(examService.getExam(examUUID)).thenReturn(exam);
        when(examService.getFragenForExam(examUUID)).thenReturn(List.of(frage));

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
                null,
                examUUID,
                "Exam 1",
                UUID.randomUUID(),
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        FrageDTO frage = new FrageDTO(
                null,
                UUID.randomUUID(),
                "F1",
                2,
                QuestionTypeDTO.MC,
                UUID.randomUUID(),
                examUUID);

        when(examService.getExam(examUUID)).thenReturn(exam);
        when(examService.getFragenForExam(examUUID)).thenReturn(List.of(frage));
        when(examService.getChoiceForFrage(frage.getFachId())).thenReturn("A, B \n C \n D");

        // Act
        ExamForm form = service.fillExamForm(examUUID);

        // Assert
        assertThat(form.getQuestions()).hasSize(1);
        assertThat(form.getQuestions().getFirst().getType()).isEqualTo("MC");
        assertThat(form.getQuestions().getFirst().getChoices()).isEqualTo("A- B,C,D");
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
        verify(examService).createFrage(argThat(f -> f.getFrageText().equals("F1")));
        verify(examService).createChoiceFrage(argThat(f -> f.getFrageText().equals("F2")), any());
        verify(examService).createChoiceFrage(argThat(f -> f.getFrageText().equals("F3")), any());
    }

    @Test
    @DisplayName("Die Korrekturgesamtübersicht für alle Exams wird korrekt ermittelt")
    void getReviewCoverage_01() {
        // Arrange
        UUID examUUID = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        ExamDTO exam = new ExamDTO(
                null,
                examUUID,
                "Exam 1",
                profFachId,
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        when(examService.reviewCoverage(examUUID)).thenReturn(50.0);

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
}
