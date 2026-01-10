package exambyte.application;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.AutomaticReviewService;
import exambyte.application.service.AutomaticReviewServiceImpl;
import exambyte.application.service.ExamManagementService;
import exambyte.application.service.ExamManagementServiceImpl;
import exambyte.domain.mapper.*;
import exambyte.domain.model.aggregate.exam.*;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.*;
import exambyte.infrastructure.mapper.*;
import exambyte.infrastructure.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExamManagementServiceTest {

    private ExamService examService;
    private AntwortService antwortService;
    private FrageService frageService;
    private StudentService studentService;
    private ProfessorService professorService;
    private KorrektorService korrektorService;
    private KorrekteAntwortenService korrekteAntwortenService;
    private ExamManagementService examManagementService;
    private ReviewService reviewService;
    private AutomaticReviewService automaticReviewService;

    private ExamDTOMapper examDTOMapper;
    private FrageDTOMapper frageDTOMapper;
    private AntwortDTOMapper antwortDTOMapper;
    private KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;
    private ReviewDTOMapper reviewDTOMapper;
    private StudentDTOMapper studentDTOMapper;

    @BeforeEach
    void setUp() {
        examService = mock(ExamServiceImpl.class);
        antwortService = mock(AntwortServiceImpl.class);
        frageService = mock(FrageServiceImpl.class);
        studentService = mock(StudentServiceImpl.class);
        professorService = mock(ProfessorServiceImpl.class);
        korrektorService = mock(KorrektorServiceImpl.class);
        korrekteAntwortenService = mock(KorrekteAntwortenServiceImpl.class);
        reviewService = mock(ReviewServiceImpl.class);
        automaticReviewService = mock(AutomaticReviewServiceImpl.class);

        examDTOMapper = mock(ExamDTOMapperImpl.class);
        frageDTOMapper = mock(FrageDTOMapperImpl.class);
        antwortDTOMapper = mock(AntwortDTOMapperImpl.class);
        reviewDTOMapper = mock(ReviewDTOMapperImpl.class);
        korrekteAntwortenDTOMapper = mock(KorrekteAntwortenDTOMapperImpl.class);
        studentDTOMapper = mock(StudentDTOMapperImpl.class);

        examManagementService = new ExamManagementServiceImpl(
                examService,
                antwortService,
                frageService,
                studentService,
                professorService,
                korrektorService,
                reviewService,
                automaticReviewService,
                korrekteAntwortenService,
                examDTOMapper,
                frageDTOMapper,
                antwortDTOMapper,
                korrekteAntwortenDTOMapper,
                reviewDTOMapper,
                studentDTOMapper);
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist erfolgreich (noch kein Exam in der DB vorhanden)")
    void createExam_01() {
        // Arrange
        Exam mockExam = mock(Exam.class);
        String profName = "Prof 1";
        String title = "Exam 1";
        UUID id = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = start.plusHours(1);
        LocalDateTime result = end.plusHours(2);


        ExamDTO dto = new ExamDTO(null, null, title, id, start, end, result);

        when(professorService.getProfessorFachIdByName(profName)).thenReturn(Optional.of(id));
        when(examService.allExams()).thenReturn(List.of());
        when(examDTOMapper.toDTO(any())).thenReturn(dto);
        when(examDTOMapper.toDomain(any(ExamDTO.class))).thenReturn(mockExam);

        // Act
        String message = examManagementService.createExam(profName, title, start, end, result);

        // Assert
        assertThat(message).isEmpty();
        verify(professorService).getProfessorFachIdByName(profName);
        verify(examService).addExam(any(Exam.class));
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist nicht erfolgreich (12 Exams sind schon vorhanden)")
    void createExam_02() {
        // Arrange
        String profName = "Prof 1";
        String title = "Exam 0";
        UUID id = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = start.plusHours(1);
        LocalDateTime result = end.plusHours(2);

        List<Exam> exams = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            exams.add(new Exam.ExamBuilder()
                    .title("Exam " + i)
                    .startTime(start.plusHours(i))
                    .endTime(end.plusHours(i))
                    .resultTime(result.plusHours(i))
                    .professorFachId(id)
                    .build());
        }

        when(professorService.getProfessorFachIdByName(profName)).thenReturn(Optional.of(id));
        when(examService.allExams()).thenReturn(exams);

        // Act
        String message = examManagementService.createExam(profName, title, start, end, result);

        // Assert
        assertThat(message).isEqualTo("Die maximale Kapazität von 12 Exams ist nun überschritten worden!");
        verify(professorService).getProfessorFachIdByName(profName);
        verify(examService, never()).addExam(any());
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist nicht erfolgreich (Exam mit selben Startzeit schon vorhanden)")
    void createExam_03() {
        // Arrange
        String profName = "Prof 1";
        String title = "Exam 1";
        UUID id = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = start.plusHours(1);
        LocalDateTime result = end.plusHours(2);

        Exam exam = new Exam.ExamBuilder()
                .title(title)
                .startTime(start)
                .endTime(end)
                .resultTime(result)
                .professorFachId(id)
                .build();

        ExamDTO examDTO = new ExamDTO(
                null,
                null,
                title,
                id,
                start,
                end.plusHours(1),
                result.plusHours(1));

        List<Exam> exams = List.of(exam);

        when(professorService.getProfessorFachIdByName(profName)).thenReturn(Optional.of(id));
        when(examService.allExams()).thenReturn(exams);
        when(examDTOMapper.toDTO(exam)).thenReturn(examDTO);

        // Act
        String message = examManagementService.createExam(profName, title, start, end, result);

        // Assert
        assertThat(message).isEqualTo("Ein Exam mit der selben Startzeit ist schon vorhanden!");
        verify(professorService).getProfessorFachIdByName(profName);
        verify(examService, never()).addExam(any());
    }

    @Test
    @DisplayName("getAllExams liefert Exams sortiert nach Startzeit")
    void getAllExams_01() {
        // Arrange
        UUID id = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = start.plusHours(10);
        LocalDateTime result = end.plusHours(20);

        Exam exam1 = new Exam.ExamBuilder()
                .fachId(UUID.randomUUID())
                .title("Exam 1")
                .startTime(start)
                .endTime(end)
                .resultTime(result)
                .professorFachId(id)
                .build();

        Exam exam2 = new Exam.ExamBuilder()
                .fachId(UUID.randomUUID())
                .title("Exam 2")
                .startTime(start.plusHours(2))
                .endTime(end)
                .resultTime(result)
                .professorFachId(id)
                .build();

        Exam exam3 = new Exam.ExamBuilder()
                .fachId(UUID.randomUUID())
                .title("Exam 2")
                .startTime(start.plusHours(1))
                .endTime(end)
                .resultTime(result)
                .professorFachId(id)
                .build();

        // Startzeiten hier: start, start + 2, start + 1
        when(examService.allExams()).thenReturn(List.of(exam1, exam2, exam3));
        when(examDTOMapper.toDTO(any(Exam.class)))
                .thenAnswer(invocation -> {
                    Exam e = invocation.getArgument(0);
                    return new ExamDTO(null, null, "x", id,
                            e.getStartTime(), null, null);
                });

        // Act
        List<ExamDTO> resultList = examManagementService.getAllExams();

        // Assert
        assertThat(resultList).extracting(ExamDTO::startTime)
                .containsExactly(start, start.plusHours(1), start.plusHours(2));
        verify(examService).allExams();
    }

    @Test
    @DisplayName("isExamAlreadySubmitted Test")
    void isExamAlreadySubmitted_01() {
        // Arrange
        UUID examFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        String studentName = "Marvin0109";

        FrageDTO frageDTO = new FrageDTO(
                null,
                UUID.randomUUID(),
                "Frage 1",
                10,
                QuestionTypeDTO.FREITEXT,
                UUID.randomUUID(),
                examFachId
        );

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(frageDTO.getFachId())
                .frageText("Frage 1")
                .maxPunkte(10)
                .type(QuestionType.FREITEXT)
                .professorUUID(frageDTO.getProfessorUUID())
                .examUUID(examFachId)
                .build();

        when(studentService.getStudentFachId(studentName)).thenReturn(studentFachId);
        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage));
        when(frageDTOMapper.toFrageDTOList(List.of(frage))).thenReturn(List.of(frageDTO));
        when(antwortService.findByStudentAndFrage(studentFachId, frageDTO.getFachId())).thenReturn(mock(Antwort.class));

        // Act
        boolean success = examManagementService.isExamAlreadySubmitted(examFachId, studentName);

        // Assert
        assertThat(success).isTrue();
    }

    @Test
    @DisplayName("Ein Exam kann erfolgreich eingereicht werden")
    void submitExam_01() {
        // Arrange
        String studentLogin = "Marvin0109";
        UUID studentFachId = UUID.randomUUID();
        UUID professorFachId = UUID.randomUUID();

        UUID examFachId = UUID.randomUUID();
        UUID frageFachId1 = UUID.randomUUID();
        UUID frageFachId2 = UUID.randomUUID();

        Map<String, List<String>> antworten = Map.of(
                frageFachId1.toString(), List.of("Antwort 1"),
                frageFachId2.toString(), List.of("Antwort 2")
        );

        Frage frage1 = new Frage.FrageBuilder()
                .fachId(frageFachId1)
                .frageText("Fragetext1")
                .maxPunkte(10)
                .type(QuestionType.SC)
                .professorUUID(professorFachId)
                .examUUID(examFachId)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(frageFachId2)
                .frageText("Fragetext2")
                .maxPunkte(5)
                .type(QuestionType.MC)
                .professorUUID(professorFachId)
                .examUUID(examFachId)
                .build();

        FrageDTO frageDTO1 = new FrageDTO(
                null,
                frage1.getFachId(),
                frage1.getFrageText(),
                frage1.getMaxPunkte(),
                QuestionTypeDTO.valueOf(frage1.getType().toString()),
                frage1.getProfessorUUID(),
                frage1.getExamUUID());

        FrageDTO frageDTO2 = new FrageDTO(
                null,
                frage2.getFachId(),
                frage2.getFrageText(),
                frage2.getMaxPunkte(),
                QuestionTypeDTO.valueOf(frage2.getType().toString()),
                frage2.getProfessorUUID(),
                frage2.getExamUUID());

        when(studentService.getStudentFachId(studentLogin)).thenReturn(studentFachId);
        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage1, frage2));
        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);

        when(antwortService.findByStudentAndFrage(any(), any())).thenReturn(mock(Antwort.class));

        when(automaticReviewService.automatischeReviewMC(any(), any(), any(), any(), any()))
                .thenReturn(List.of());
        when(automaticReviewService.automatischeReviewSC(any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        // Act
        boolean result = examManagementService.submitExam(studentLogin, antworten, examFachId);

        // Assert
        assertTrue(result);

        verify(antwortService, times(2)).addAntwort(any());

        verify(automaticReviewService).automatischeReviewMC(any(), any(), any(), eq(studentFachId), any());
        verify(automaticReviewService).automatischeReviewSC(any(), any(), any(), eq(studentFachId), any());
    }

    @Test
    @DisplayName("Ein Exam wird erfolgreich gefunden nach der Startzeit")
    void getExamByStartTime_01() {
        // Arrange
        UUID examId = UUID.randomUUID();
        UUID profUUID = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);

        Exam exam = new Exam.ExamBuilder()
                .id(null)
                .fachId(examId)
                .title("Exam 1")
                .startTime(start)
                .endTime(start.plusHours(1))
                .resultTime(start.plusHours(2))
                .professorFachId(profUUID)
                .build();

        ExamDTO examDTO = new ExamDTO(
                null,
                examId,
                "Exam 1",
                profUUID,
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        when(examService.allExams()).thenReturn(List.of(exam));
        when(examDTOMapper.toDTO(exam)).thenReturn(examDTO);

        // Act
        UUID result = examManagementService.getExamByStartTime(start);

        // Assert
        assertThat(result).isEqualTo(examId);
    }

    @Test
    @DisplayName("Ein Exam wurde nicht gefunden mit der gegebenen Startzeit")
    void getExamByStartTime_02() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);

        Exam exam = new Exam.ExamBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .title("Exam 1")
                .startTime(start.plusHours(1))
                .endTime(start.plusHours(2))
                .resultTime(start.plusHours(3))
                .professorFachId(UUID.randomUUID())
                .build();

        ExamDTO examDTO = new ExamDTO(
                null,
                exam.getFachId(),
                "Exam 1",
                exam.getProfessorFachId(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getResultTime()
        );

        when(examService.allExams()).thenReturn(List.of(exam));
        when(examDTOMapper.toDTO(exam)).thenReturn(examDTO);

        // Act
        UUID result = examManagementService.getExamByStartTime(start);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("reset Test")
    void reset_01() {
        // Act
        examManagementService.reset();

        // Assert
        InOrder inOrder = inOrder(
                reviewService,
                antwortService,
                korrekteAntwortenService,
                frageService,
                examService
        );

        inOrder.verify(reviewService).deleteAll();
        inOrder.verify(antwortService).deleteAll();
        inOrder.verify(korrekteAntwortenService).deleteAll();
        inOrder.verify(frageService).deleteAll();
        inOrder.verify(examService).deleteAll();

        verifyNoMoreInteractions(
                reviewService,
                antwortService,
                korrekteAntwortenService,
                frageService,
                examService
        );
    }

    @Test
    @DisplayName("Das entfernen von alten Antworten und die dazugehörigen Reviews ist erfolgreich")
    void removeOldAnswers_01() {
        UUID examFachId = UUID.randomUUID();
        String studentName = "Marvin0109";
        UUID studentFachId = UUID.randomUUID();

        UUID profFachId = UUID.randomUUID();

        UUID frageFachId1 = UUID.randomUUID();
        UUID frageFachId2 = UUID.randomUUID();

        UUID antwortFachId1 = UUID.randomUUID();
        UUID antwortFachId2 = UUID.randomUUID();

        UUID reviewFachId1 = UUID.randomUUID();

        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        FrageDTO frageDTO1 = new FrageDTO(
                null,
                frageFachId1,
                "F1",
                10,
                QuestionTypeDTO.FREITEXT,
                profFachId,
                examFachId);

        FrageDTO frageDTO2 = new FrageDTO(
                null,
                frageFachId2,
                "F2",
                10,
                QuestionTypeDTO.FREITEXT,
                profFachId,
                examFachId);

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(antwortFachId1)
                .antwortText("A1")
                .frageFachId(frageFachId1)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .fachId(antwortFachId2)
                .antwortText("A2")
                .frageFachId(frageFachId2)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        Review review = new Review.ReviewBuilder()
                .id(null)
                .fachId(reviewFachId1)
                .antwortFachId(antwortFachId1)
                .korrektorFachId(UUID.randomUUID())
                .bewertung("B1")
                .punkte(5)
                .build();

        when(studentService.getStudentFachId(studentName)).thenReturn(studentFachId);
        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(
                mock(Frage.class), mock(Frage.class)
        ));

        when(frageDTOMapper.toDTO(any()))
                .thenReturn(frageDTO1)
                .thenReturn(frageDTO2);

        when(antwortService.findByStudentAndFrage(studentFachId, frageFachId1)).thenReturn(antwort);
        when(antwortService.findByStudentAndFrage(studentFachId, frageFachId2)).thenReturn(antwort2);

        when(reviewService.getReviewByAntwortFachId(antwortFachId1)).thenReturn(review);
        when(reviewService.getReviewByAntwortFachId(antwortFachId2)).thenReturn(null);

        // Act
        examManagementService.removeOldAnswers(examFachId, studentName);

        // Assert
        verify(antwortService).deleteAnswer(antwortFachId1);
        verify(antwortService).deleteAnswer(antwortFachId2);

        verify(reviewService).deleteReview(reviewFachId1);
        verify(reviewService, never()).deleteReview(null);
    }

    @Test
    @DisplayName("getSubmission ist erfolgreich")
    void getSubmission_01() {
        // Arrange
        String studentName = "Marvin0109";
        UUID studentFachId = UUID.randomUUID();

        when(studentService.getStudentFachId(studentName)).thenReturn(studentFachId);

        UUID frageFachId = UUID.randomUUID();

        UUID examFachId = UUID.randomUUID();

        UUID antwortFachId = UUID.randomUUID();

        UUID korrektorFachId = UUID.randomUUID();

        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime lastChangesZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Frage frage = new Frage.FrageBuilder()
                .fachId(frageFachId)
                .examUUID(examFachId)
                .maxPunkte(5)
                .type(QuestionType.MC)
                .frageText("Testfrage")
                .professorUUID(UUID.randomUUID())
                .build();

        FrageDTO frageDTO = new FrageDTO(
                null,
                frageFachId,
                "Testfrage",
                5,
                QuestionTypeDTO.MC,
                UUID.randomUUID(),
                examFachId);

        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);

        AntwortDTO antwortDTO = new AntwortDTO.AntwortDTOBuilder()
                .fachId(antwortFachId)
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortText("Antwort")
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(lastChangesZeitpunkt)
                .build();

        Antwort antwortDomain = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(antwortFachId)
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortText("Antwort")
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(lastChangesZeitpunkt)
                .build();

        when(antwortService.findByStudentAndFrage(studentFachId, frageFachId)).thenReturn(antwortDomain);
        when(antwortDTOMapper.toDTO(antwortDomain)).thenReturn(antwortDTO);

        Review review = new Review.ReviewBuilder()
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorFachId)
                .bewertung("Gut")
                .punkte(5)
                .build();

        ReviewDTO reviewDTO = new ReviewDTO(
                null,
                UUID.randomUUID(),
                antwortFachId,
                korrektorFachId,
                "Gut",
                5);

        when(reviewService.getReviewByAntwortFachId(antwortFachId)).thenReturn(review);
        when(reviewDTOMapper.toDTO(review)).thenReturn(reviewDTO);

        // Act
        VersuchDTO attempt = examManagementService.getSubmission(examFachId, "Marvin0109");

        // Assert
        assertThat(attempt.erreichtePunkte()).isEqualTo(5.0);
        assertThat(attempt.maxPunkte()).isEqualTo(5.0);
        assertThat(attempt.prozent()).isEqualTo(100.00);
        assertThat(attempt.lastChanges()).isEqualTo(lastChangesZeitpunkt);
    }

    @Test
    @DisplayName("Laden des Bewertungsstatus ist erfolgreich (1 von 2 Freitext-Antworten hat Review)")
    void reviewCoverage_01() {
        // Arrange
        UUID examFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Frage frage1 = new Frage.FrageBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .frageText("F1")
                .maxPunkte(2)
                .type(QuestionType.FREITEXT)
                .professorUUID(UUID.randomUUID())
                .examUUID(examFachId)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .frageText("F2")
                .maxPunkte(2)
                .type(QuestionType.FREITEXT)
                .professorUUID(UUID.randomUUID())
                .examUUID(examFachId)
                .build();

        FrageDTO frageDTO1 = new FrageDTO(
                null,
                frage1.getFachId(),
                "F1",
                2,
                QuestionTypeDTO.FREITEXT,
                frage1.getProfessorUUID(),
                examFachId
        );

        FrageDTO frageDTO2 = new FrageDTO(
                null,
                frage2.getFachId(),
                "F2",
                2,
                QuestionTypeDTO.FREITEXT,
                frage2.getProfessorUUID(),
                examFachId
        );

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .antwortText("A1")
                .frageFachId(frage1.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .antwortText("A2")
                .frageFachId(frage2.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        AntwortDTO antwortDTO1 = new AntwortDTO.AntwortDTOBuilder()
                .id(null)
                .fachId(antwort1.getFachId())
                .antwortText("A1")
                .frageFachId(frageDTO1.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        AntwortDTO antwortDTO2 = new AntwortDTO.AntwortDTOBuilder()
                .id(null)
                .fachId(antwort2.getFachId())
                .antwortText("A2")
                .frageFachId(frageDTO2.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        Review r1 = new Review.ReviewBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortDTO1.getFachId())
                .korrektorFachId(UUID.randomUUID())
                .bewertung("Gut")
                .punkte(1)
                .build();

        ReviewDTO r1DTO = new ReviewDTO(
                null,
                r1.getFachId(),
                antwortDTO1.getFachId(),
                r1.getKorrektorFachId(),
                r1.getBewertung(),
                r1.getPunkte()
        );

        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage1, frage2));

        when(antwortService.findByFrageFachId(frage1.getFachId())).thenReturn(antwort1);
        when(antwortService.findByFrageFachId(frage2.getFachId())).thenReturn(antwort2);

        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwortDTO1);
        when(antwortDTOMapper.toDTO(antwort2)).thenReturn(antwortDTO2);

        when(reviewService.getReviewByAntwortFachId(antwortDTO1.getFachId())).thenReturn(r1);
        when(reviewService.getReviewByAntwortFachId(antwortDTO2.getFachId())).thenReturn(null);

        when(reviewDTOMapper.toDTO(r1)).thenReturn(r1DTO);

        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);

        // Act
        double coverage = examManagementService.reviewCoverage(examFachId);

        // Assert
        assertThat(coverage).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Laden des Bewertungsstatus ist erfolgreich (1 von 1 Freitext-Antwort hat Review)")
    void reviewCoverage_02() {
        // Arrange
        UUID examFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

        Frage frage1 = new Frage.FrageBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .frageText("F1")
                .maxPunkte(2)
                .type(QuestionType.FREITEXT)
                .professorUUID(UUID.randomUUID())
                .examUUID(examFachId)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .frageText("F2")
                .maxPunkte(2)
                .type(QuestionType.MC)
                .professorUUID(UUID.randomUUID())
                .examUUID(examFachId)
                .build();

        FrageDTO frageDTO1 = new FrageDTO(
                null,
                frage1.getFachId(),
                "F1",
                2,
                QuestionTypeDTO.FREITEXT,
                frage1.getProfessorUUID(),
                examFachId
        );

        FrageDTO frageDTO2 = new FrageDTO(
                null,
                frage2.getFachId(),
                "F2",
                2,
                QuestionTypeDTO.MC,
                frage2.getProfessorUUID(),
                examFachId
        );

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .antwortText("A1")
                .frageFachId(frage1.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .antwortText("A2")
                .frageFachId(frage2.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        AntwortDTO antwortDTO1 = new AntwortDTO.AntwortDTOBuilder()
                .id(null)
                .fachId(antwort1.getFachId())
                .antwortText("A1")
                .frageFachId(frageDTO1.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        AntwortDTO antwortDTO2 = new AntwortDTO.AntwortDTOBuilder()
                .id(null)
                .fachId(antwort2.getFachId())
                .antwortText("A2")
                .frageFachId(frageDTO2.getFachId())
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        Review r1 = new Review.ReviewBuilder()
                .id(null)
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortDTO1.getFachId())
                .korrektorFachId(UUID.randomUUID())
                .bewertung("Gut")
                .punkte(1)
                .build();

        ReviewDTO r1DTO = new ReviewDTO(
                null,
                r1.getFachId(),
                antwortDTO1.getFachId(),
                r1.getKorrektorFachId(),
                r1.getBewertung(),
                r1.getPunkte()
        );

        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage1, frage2));

        when(antwortService.findByFrageFachId(frage1.getFachId())).thenReturn(antwort1);
        when(antwortService.findByFrageFachId(frage2.getFachId())).thenReturn(antwort2);

        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwortDTO1);
        when(antwortDTOMapper.toDTO(antwort2)).thenReturn(antwortDTO2);

        when(reviewService.getReviewByAntwortFachId(antwortDTO1.getFachId())).thenReturn(r1);
        when(reviewService.getReviewByAntwortFachId(antwortDTO2.getFachId())).thenReturn(null);

        when(reviewDTOMapper.toDTO(r1)).thenReturn(r1DTO);

        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);

        // Act
        double coverage = examManagementService.reviewCoverage(examFachId);

        // Assert

        // 2 Fragen: MC und Freitextaufgaben, aber von den Freitextaufgaben haben alle eine Bewertung
        assertThat(coverage).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Freitextantworten aller Studenten wird erfolgreich gefunden und nach Namen gruppiert")
    void getStudentSubmittedExam_01() {
        // Arrange
        UUID studentFachId = UUID.randomUUID();
        UUID examFachId = UUID.randomUUID();
        UUID antwortFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();
        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2026, 1, 1, 0, 0);

        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(studentFachId)
                .name("Marvin0109")
                .build();

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(frageFachId)
                .frageText("F1")
                .maxPunkte(10)
                .type(QuestionType.FREITEXT)
                .professorUUID(profFachId)
                .examUUID(examFachId)
                .build();

        FrageDTO frageDTO = new FrageDTO(
                null,
                frageFachId,
                "F1",
                10,
                QuestionTypeDTO.FREITEXT,
                profFachId,
                examFachId
        );

        StudentDTO studentDTO = new StudentDTO(null, studentFachId, "Marvin0109");

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(antwortFachId)
                .antwortText("Antwort 1")
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(antwortZeitpunkt)
                .build();

        AntwortDTO antwort1DTO = new AntwortDTO.AntwortDTOBuilder()
            .id(null)
            .fachId(antwortFachId)
            .antwortText("Antwort 1")
            .frageFachId(frageFachId)
            .studentFachId(studentFachId)
            .antwortZeitpunkt(antwortZeitpunkt)
            .lastChangesZeitpunkt(antwortZeitpunkt)
            .build();

        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);

        when(antwortService.findByFrageFachId(frageFachId)).thenReturn(antwort1);
        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwort1DTO);

        when(studentService.getStudent(studentFachId)).thenReturn(student);
        when(studentDTOMapper.toDTO(student)).thenReturn(studentDTO);

        // Act
        List<StudentDTO> result = examManagementService.getStudentSubmittedExam(examFachId);

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst()).isEqualTo(studentDTO);
    }
}
