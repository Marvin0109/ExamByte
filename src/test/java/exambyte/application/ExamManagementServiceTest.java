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

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamManagementServiceTest {

    private ExamService examService;
    private AntwortService antwortService;
    private FrageService frageService;
    private StudentService studentService;
    private ProfessorService professorService;
    private KorrekteAntwortenService korrekteAntwortenService;
    private ExamManagementService examManagementService;
    private ReviewService reviewService;
    private AutomaticReviewService automaticReviewService;

    private ExamDTOMapper examDTOMapper;
    private FrageDTOMapper frageDTOMapper;
    private AntwortDTOMapper antwortDTOMapper;
    private ReviewDTOMapper reviewDTOMapper;
    private StudentDTOMapper studentDTOMapper;
    private ProfessorDTOMapper professorDTOMapper;

    private static final String STUDENT_NAME = "Marvin0109";
    private static final String PROF_NAME = "Prof";

    private static final UUID STUDENT_UUID = UUID.randomUUID();
    private static final UUID PROF_UUID = UUID.randomUUID();
    private static final UUID EXAM_UUID = UUID.randomUUID();
    private static final LocalDateTime TIME_VAR =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        examService = mock(ExamServiceImpl.class);
        antwortService = mock(AntwortServiceImpl.class);
        frageService = mock(FrageServiceImpl.class);
        studentService = mock(StudentServiceImpl.class);
        professorService = mock(ProfessorServiceImpl.class);
        KorrektorService korrektorService = mock(KorrektorServiceImpl.class);
        korrekteAntwortenService = mock(KorrekteAntwortenServiceImpl.class);
        reviewService = mock(ReviewServiceImpl.class);
        automaticReviewService = mock(AutomaticReviewServiceImpl.class);

        examDTOMapper = mock(ExamDTOMapperImpl.class);
        frageDTOMapper = mock(FrageDTOMapperImpl.class);
        antwortDTOMapper = mock(AntwortDTOMapperImpl.class);
        reviewDTOMapper = mock(ReviewDTOMapperImpl.class);
        KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper = mock(KorrekteAntwortenDTOMapperImpl.class);
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
                studentDTOMapper,
                professorDTOMapper);
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist erfolgreich (noch kein Exam in der DB vorhanden)")
    void createExam_01() {
        // Arrange
        Exam mockExam = mock(Exam.class);
        LocalDateTime end = TIME_VAR.plusHours(1);
        LocalDateTime result = end.plusHours(2);

        ExamDTO dto = new ExamDTO(null, "Exam 1", PROF_UUID, TIME_VAR, end, result);

        when(professorService.getProfessorFachIdByName(PROF_NAME)).thenReturn(Optional.of(PROF_UUID));
        when(examService.allExams()).thenReturn(List.of());
        when(examDTOMapper.toDTO(any())).thenReturn(dto);
        when(examDTOMapper.toDomain(any(ExamDTO.class))).thenReturn(mockExam);

        // Act
        String message = examManagementService.createExam(PROF_NAME, "Exam 1", TIME_VAR, end, result);

        // Assert
        assertThat(message).isEmpty();
        verify(professorService).getProfessorFachIdByName(PROF_NAME);
        verify(examService).addExam(any(Exam.class));
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist nicht erfolgreich (12 Exams sind schon vorhanden)")
    void createExam_02() {
        // Arrange
        LocalDateTime end = TIME_VAR.plusHours(1);
        LocalDateTime result = end.plusHours(2);

        List<Exam> exams = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            exams.add(new Exam.ExamBuilder()
                    .title("Exam " + i)
                    .startTime(TIME_VAR.plusHours(i))
                    .endTime(end.plusHours(i))
                    .resultTime(result.plusHours(i))
                    .professorFachId(PROF_UUID)
                    .build());
        }

        when(professorService.getProfessorFachIdByName(PROF_NAME)).thenReturn(Optional.of(PROF_UUID));
        when(examService.allExams()).thenReturn(exams);

        // Act
        String message = examManagementService.createExam(PROF_NAME, "Exam 0", TIME_VAR, end, result);

        // Assert
        assertThat(message).isEqualTo("Die maximale Kapazität von 12 Exams ist nun überschritten worden!");
        verify(professorService).getProfessorFachIdByName(PROF_NAME);
        verify(examService, never()).addExam(any());
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist nicht erfolgreich (Exam mit selben Startzeit schon vorhanden)")
    void createExam_03() {
        // Arrange
        LocalDateTime end = TIME_VAR.plusHours(1);
        LocalDateTime result = end.plusHours(2);

        Exam exam = new Exam.ExamBuilder()
                .title("Exam 1")
                .startTime(TIME_VAR)
                .endTime(end)
                .resultTime(result)
                .professorFachId(PROF_UUID)
                .build();

        ExamDTO examDTO = new ExamDTO(
                null,
                "Exam 1",
                PROF_UUID,
                TIME_VAR,
                end.plusHours(1),
                result.plusHours(1));

        List<Exam> exams = List.of(exam);

        when(professorService.getProfessorFachIdByName(PROF_NAME)).thenReturn(Optional.of(PROF_UUID));
        when(examService.allExams()).thenReturn(exams);
        when(examDTOMapper.toDTO(exam)).thenReturn(examDTO);

        // Act
        String message = examManagementService.createExam(PROF_NAME, "Exam 1", TIME_VAR, end, result);

        // Assert
        assertThat(message).isEqualTo("Ein Exam mit der selben Startzeit ist schon vorhanden!");
        verify(professorService).getProfessorFachIdByName(PROF_NAME);
        verify(examService, never()).addExam(any());
    }

    @Test
    @DisplayName("Professor wurde nicht gefunden")
    void createExam_04() {
        LocalDateTime end = TIME_VAR.plusHours(1);
        LocalDateTime result = end.plusHours(2);

        when(professorService.getProfessorFachIdByName(PROF_NAME)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class,
                () -> examManagementService.createExam(PROF_NAME, "Exam 1", TIME_VAR, end, result));
    }

    @Test
    @DisplayName("Startzeit ist invalide")
    void createExam_05() {
        LocalDateTime end = TIME_VAR.minusHours(1);
        LocalDateTime result = TIME_VAR.plusHours(1);

        when(professorService.getProfessorFachIdByName(PROF_NAME)).thenReturn(Optional.of(PROF_UUID));

        String message = examManagementService.createExam(PROF_NAME, "Exam 1", TIME_VAR, end, result);
        assertThat(message).isEqualTo("Start-Zeitpunkt muss vor End-Zeitpunkt liegen!");
    }

    @Test
    @DisplayName("Ergebnis-Zeitpunkt ist invalide")
    void createExam_06() {
        LocalDateTime end = TIME_VAR.plusHours(10);
        LocalDateTime result = TIME_VAR.plusHours(1);

        when(professorService.getProfessorFachIdByName(PROF_NAME)).thenReturn(Optional.of(PROF_UUID));

        String message = examManagementService.createExam(PROF_NAME, "Exam 1", TIME_VAR, end, result);
        assertThat(message).isEqualTo("Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!");
    }

    @Test
    void getAllExams_sortedASC() {
        // Arrange
        Exam exam1 = new Exam.ExamBuilder()
                .startTime(TIME_VAR.plusHours(2))
                .professorFachId(PROF_UUID)
                .build();
        Exam exam2 = new Exam.ExamBuilder()
                .startTime(TIME_VAR)
                .professorFachId(PROF_UUID)
                .build();
        Exam exam3 = new Exam.ExamBuilder()
                .startTime(TIME_VAR.plusHours(1))
                .professorFachId(PROF_UUID)
                .build();

        when(examService.allExams()).thenReturn(List.of(exam1, exam2, exam3));
        when(examDTOMapper.toDTO(any())).thenAnswer(i -> {
            Exam e = i.getArgument(0);
            return new ExamDTO(null, "x", PROF_UUID, e.getStartTime(), null, null);
        });

        // Act
        List<ExamDTO> exams = examManagementService.getAllExams();

        // Assert
        assertThat(exams)
                .extracting(ExamDTO::startTime)
                .containsExactly(TIME_VAR, TIME_VAR.plusHours(1), TIME_VAR.plusHours(2));

        verify(examService).allExams();
        verify(examDTOMapper, times(3)).toDTO(any());
    }

    @Test
    void isExamAlreadySubmitted_Yes() {
        // Arrange
        FrageDTO frageDTO = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        Frage frage = new Frage.FrageBuilder()
                .fachId(frageDTO.fachId())
                .frageText("Frage 1")
                .maxPunkte(10)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toFrageDTOList(List.of(frage))).thenReturn(List.of(frageDTO));
        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageDTO.fachId())).thenReturn(mock(Antwort.class));

        // Act
        boolean success = examManagementService.isExamAlreadySubmitted(EXAM_UUID, STUDENT_NAME);

        // Assert
        assertThat(success).isTrue();
        verify(studentService).getStudentFachId(STUDENT_NAME);
        verify(frageService).getFragenForExam(EXAM_UUID);
        verify(antwortService).findByStudentAndFrage(STUDENT_UUID, frageDTO.fachId());
    }

    @Test
    void isExamAlreadySubmitted_No() {
        // Arrange
        FrageDTO frageDTO = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        Frage frage = new Frage.FrageBuilder()
                .fachId(frageDTO.fachId())
                .frageText("Frage 1")
                .maxPunkte(10)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toFrageDTOList(List.of(frage))).thenReturn(List.of(frageDTO));
        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageDTO.fachId())).thenReturn(null);

        // Act
        boolean success = examManagementService.isExamAlreadySubmitted(EXAM_UUID, STUDENT_NAME);

        // Assert
        assertThat(success).isFalse();
        verify(studentService).getStudentFachId(STUDENT_NAME);
        verify(frageService).getFragenForExam(EXAM_UUID);
        verify(antwortService).findByStudentAndFrage(STUDENT_UUID, frageDTO.fachId());
    }

    @Test
    @DisplayName("Ist mind. 1 Antwort vorhanden, dann bricht die Suche ab")
    void isExamAlreadySubmitted_OnlyOneOfTwo() {
        // Arrange
        FrageDTO frage1DTO = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        FrageDTO frage2DTO = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                5,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        Frage frage1 = new Frage.FrageBuilder()
                .fachId(frage1DTO.fachId())
                .frageText("Frage 1")
                .maxPunkte(10)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(frage2DTO.fachId())
                .frageText("Frage 2")
                .maxPunkte(5)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage1, frage2));
        when(frageDTOMapper.toFrageDTOList(List.of(frage1, frage2))).thenReturn(List.of(frage1DTO, frage2DTO));

        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frage1DTO.fachId()))
                .thenReturn(mock(Antwort.class));

        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frage2DTO.fachId()))
                .thenReturn(null);

        // Act
        boolean submitted = examManagementService.isExamAlreadySubmitted(EXAM_UUID, STUDENT_NAME);

        // Assert
        assertThat(submitted).isTrue();
        verify(studentService).getStudentFachId(STUDENT_NAME);
        verify(frageService).getFragenForExam(EXAM_UUID);
        verify(antwortService).findByStudentAndFrage(STUDENT_UUID, frage1DTO.fachId());
        verify(antwortService, never()).findByStudentAndFrage(STUDENT_UUID, frage2DTO.fachId());
    }

    @Test
    void submitExam_Success() {
        // Arrange
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
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(frageFachId2)
                .frageText("Fragetext2")
                .maxPunkte(5)
                .type(QuestionType.MC)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        FrageDTO frageDTO1 = new FrageDTO(
                frageFachId1,
                "Fragetext1",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.SC);

        FrageDTO frageDTO2 = new FrageDTO(
                frageFachId2,
                "FrageText2",
                5,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.MC);

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage1, frage2));
        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);

        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageFachId1)).thenReturn(mock(Antwort.class));
        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageFachId2)).thenReturn(mock(Antwort.class));

        when(automaticReviewService.automatischeReviewMC(any(), any(), any(), eq(STUDENT_UUID), any()))
                .thenReturn(List.of());
        when(automaticReviewService.automatischeReviewSC(any(), any(), any(), eq(STUDENT_UUID), any()))
                .thenReturn(List.of());

        // Act
        boolean result = examManagementService.submitExam(STUDENT_NAME, antworten, EXAM_UUID);

        // Assert
        assertTrue(result);

        verify(antwortService, times(2)).addAntwort(any());

        verify(automaticReviewService).automatischeReviewMC(any(), any(), any(), eq(STUDENT_UUID), any());
        verify(automaticReviewService).automatischeReviewSC(any(), any(), any(), eq(STUDENT_UUID), any());
    }

    @Test
    void submitExam_Fail_StudentNotFound() {
        // Arrange
        when(studentService.getStudentFachId(STUDENT_NAME)).thenThrow(new RuntimeException("Nicht gefunden"));

        // Act
        boolean result = examManagementService.submitExam(STUDENT_NAME, Map.of(), EXAM_UUID);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("AntwortService wirft Exception -> false")
    void submitExam_AntwortServiceFails() {
        // Arrange
        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        Map<String, List<String>> antworten = Map.of(UUID.randomUUID().toString(), List.of("Antwort"));
        doThrow(new RuntimeException("DB Error")).when(antwortService).addAntwort(any());

        // Act
        boolean result = examManagementService.submitExam(STUDENT_NAME, antworten, EXAM_UUID);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("submitExam: ReviewService wirft Exception -> false")
    void submitExam_ReviewServiceFails() {
        // Arrange
        ReviewDTO rDTO = new ReviewDTO(null, null, null, "", 0);
        Review r = new Review.ReviewBuilder().build();

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);

        Map<String, List<String>> antworten = Map.of(UUID.randomUUID().toString(), List.of("Antwort"));

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of());

        when(automaticReviewService.automatischeReviewMC(any(), any(), any(), eq(STUDENT_UUID), any()))
                .thenReturn(List.of(rDTO));
        when(automaticReviewService.automatischeReviewSC(any(), any(), any(), eq(STUDENT_UUID), any()))
                .thenReturn(List.of());

        when(reviewDTOMapper.toDomain(rDTO)).thenReturn(r);

        doThrow(new RuntimeException("Review DB Error")).when(reviewService).addReview(r);

        // Act
        boolean result = examManagementService.submitExam(STUDENT_NAME, antworten, EXAM_UUID);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Ein Exam wird erfolgreich gefunden nach der Startzeit")
    void getExamByStartTime_01() {
        // Arrange
        Exam exam = new Exam.ExamBuilder()
                .fachId(EXAM_UUID)
                .title("Exam 1")
                .startTime(TIME_VAR)
                .endTime(TIME_VAR.plusHours(1))
                .resultTime(TIME_VAR.plusHours(2))
                .professorFachId(PROF_UUID)
                .build();

        ExamDTO examDTO = new ExamDTO(
                EXAM_UUID,
                "Exam 1",
                PROF_UUID,
                TIME_VAR,
                TIME_VAR.plusHours(1),
                TIME_VAR.plusHours(2)
        );

        when(examService.allExams()).thenReturn(List.of(exam));
        when(examDTOMapper.toDTO(exam)).thenReturn(examDTO);

        // Act
        UUID result = examManagementService.getExamByStartTime(TIME_VAR);

        // Assert
        assertThat(result).isEqualTo(EXAM_UUID);
    }

    @Test
    @DisplayName("Ein Exam wurde nicht gefunden mit der gegebenen Startzeit")
    void getExamByStartTime_02() {
        // Arrange
        Exam exam = new Exam.ExamBuilder()
                .fachId(UUID.randomUUID())
                .title("Exam 1")
                .startTime(TIME_VAR.plusHours(1))
                .endTime(TIME_VAR.plusHours(2))
                .resultTime(TIME_VAR.plusHours(3))
                .professorFachId(PROF_UUID)
                .build();

        ExamDTO examDTO = new ExamDTO(
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
        UUID result = examManagementService.getExamByStartTime(TIME_VAR);

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
    void removeOldAnswers_Success() {
        UUID frageFachId1 = UUID.randomUUID();
        UUID frageFachId2 = UUID.randomUUID();

        UUID antwortFachId1 = UUID.randomUUID();
        UUID antwortFachId2 = UUID.randomUUID();

        UUID reviewFachId1 = UUID.randomUUID();

        FrageDTO frageDTO1 = new FrageDTO(
                frageFachId1,
                "F1",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        FrageDTO frageDTO2 = new FrageDTO(
                frageFachId2,
                "F2",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(antwortFachId1)
                .antwortText("A1")
                .frageFachId(frageFachId1)
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .fachId(antwortFachId2)
                .antwortText("A2")
                .frageFachId(frageFachId2)
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        Review review = new Review.ReviewBuilder()
                .fachId(reviewFachId1)
                .antwortFachId(antwortFachId1)
                .korrektorFachId(UUID.randomUUID())
                .bewertung("B1")
                .punkte(5)
                .build();

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(
                mock(Frage.class), mock(Frage.class)
        ));

        when(frageDTOMapper.toDTO(any()))
                .thenReturn(frageDTO1)
                .thenReturn(frageDTO2);

        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageFachId1)).thenReturn(antwort);
        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageFachId2)).thenReturn(antwort2);

        when(reviewService.getReviewByAntwortFachId(antwortFachId1)).thenReturn(review);
        when(reviewService.getReviewByAntwortFachId(antwortFachId2)).thenReturn(null);

        // Act
        examManagementService.removeOldAnswers(EXAM_UUID, STUDENT_NAME);

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
        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);

        UUID frageFachId = UUID.randomUUID();
        UUID antwortFachId = UUID.randomUUID();
        UUID korrektorFachId = UUID.randomUUID();

        Frage frage = new Frage.FrageBuilder()
                .fachId(frageFachId)
                .examUUID(EXAM_UUID)
                .maxPunkte(5)
                .type(QuestionType.MC)
                .frageText("Testfrage")
                .professorUUID(UUID.randomUUID())
                .build();

        FrageDTO frageDTO = new FrageDTO(
                frageFachId,
                "Testfrage",
                5,
                UUID.randomUUID(),
                EXAM_UUID,
                QuestionTypeDTO.MC);

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);

        AntwortDTO antwortDTO = new AntwortDTO(
                antwortFachId,
                "Antwort",
                frageFachId,
                STUDENT_UUID,
                TIME_VAR);

        Antwort antwortDomain = new Antwort.AntwortBuilder()
                .fachId(antwortFachId)
                .frageFachId(frageFachId)
                .studentFachId(STUDENT_UUID)
                .antwortText("Antwort")
                .antwortZeitpunkt(TIME_VAR)
                .build();

        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageFachId)).thenReturn(antwortDomain);
        when(antwortDTOMapper.toDTO(antwortDomain)).thenReturn(antwortDTO);

        Review review = new Review.ReviewBuilder()
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorFachId)
                .bewertung("Gut")
                .punkte(5)
                .build();

        ReviewDTO reviewDTO = new ReviewDTO(
                UUID.randomUUID(),
                antwortFachId,
                korrektorFachId,
                "Gut",
                5);

        when(reviewService.getReviewByAntwortFachId(antwortFachId)).thenReturn(review);
        when(reviewDTOMapper.toDTO(review)).thenReturn(reviewDTO);

        // Act
        VersuchDTO attempt = examManagementService.getSubmission(EXAM_UUID, STUDENT_NAME);

        // Assert
        assertThat(attempt.erreichtePunkte()).isEqualTo(5.0);
        assertThat(attempt.maxPunkte()).isEqualTo(5.0);
        assertThat(attempt.prozent()).isEqualTo(100.00);
    }

    @Test
    void getSubmission_NoAnswers() {
        // Arrange
        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);
        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of());

        // Act
        VersuchDTO attempt = examManagementService.getSubmission(EXAM_UUID, STUDENT_NAME);

        // Assert
        assertThat(attempt.erreichtePunkte()).isEqualTo(0.0);
        assertThat(attempt.maxPunkte()).isEqualTo(0.0);
        assertThat(attempt.prozent()).isEqualTo(0.0);
    }

    @Test
    void getSubmission_AnswerNoReview() {
        // Arrange
        UUID frageFachId = UUID.randomUUID();
        UUID antwortFachId = UUID.randomUUID();

        when(studentService.getStudentFachId(STUDENT_NAME)).thenReturn(STUDENT_UUID);

        Frage frage = new Frage.FrageBuilder()
                .fachId(frageFachId)
                .examUUID(EXAM_UUID)
                .maxPunkte(5)
                .build();

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(
                new FrageDTO(
                    frageFachId,
                    "Text",
                    5,
                    UUID.randomUUID(),
                    EXAM_UUID,
                    QuestionTypeDTO.MC));

        Antwort antwortDomain = new Antwort.AntwortBuilder()
                .fachId(antwortFachId)
                .frageFachId(frageFachId)
                .studentFachId(STUDENT_UUID)
                .antwortText("Antwort")
                .antwortZeitpunkt(TIME_VAR)
                .build();

        when(antwortService.findByStudentAndFrage(STUDENT_UUID, frageFachId)).thenReturn(antwortDomain);
        when(antwortDTOMapper.toDTO(antwortDomain)).thenReturn(
                new AntwortDTO(antwortFachId, "Antwort", frageFachId, STUDENT_UUID, TIME_VAR));

        when(reviewService.getReviewByAntwortFachId(antwortFachId)).thenReturn(null);

        // Act
        VersuchDTO attempt = examManagementService.getSubmission(EXAM_UUID, STUDENT_NAME);

        // Assert
        assertThat(attempt.erreichtePunkte()).isEqualTo(0.0);
        assertThat(attempt.maxPunkte()).isEqualTo(5.0);
        assertThat(attempt.prozent()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Antworten sind korrigiert worden")
    void isSubmitBeingReviewed_true() {
        // Arrange
        Frage frage = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .frageText("Frage")
                .examUUID(EXAM_UUID)
                .type(QuestionType.FREITEXT)
                .maxPunkte(1)
                .professorUUID(PROF_UUID)
                .build();

        FrageDTO frageDTO = new FrageDTO(
                frage.getFachId(),
                "Frage",
                1,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT
        );

        List<AntwortDTO> antworten = List.of(
                new AntwortDTO(UUID.randomUUID(), "A1", frage.getFachId(), STUDENT_UUID, TIME_VAR)
        );

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(antworten.getFirst().fachId())
                .antwortText("A1")
                .frageFachId(frage.getFachId())
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);

        when(antwortService.findByFrageFachId(frage.getFachId())).thenReturn(antwort);
        when(antwortDTOMapper.toDTO(antwort)).thenReturn(antworten.getFirst());

        for (AntwortDTO a : antworten) {
            when(reviewService.getReviewByAntwortFachId(a.fachId())).thenReturn(mock(Review.class));
        }

        // Act
        boolean result = examManagementService.isSubmitBeingReviewed(
                EXAM_UUID, new StudentDTO(STUDENT_UUID,"Student"));

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Es gibt keine Korrektur zu einer Antwort")
    void isSubmitBeingReviewed_reviewNotFound() {
        // Arrange
        Frage frage = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .frageText("Frage")
                .examUUID(EXAM_UUID)
                .type(QuestionType.FREITEXT)
                .maxPunkte(1)
                .professorUUID(PROF_UUID)
                .build();

        FrageDTO frageDTO = new FrageDTO(
                frage.getFachId(),
                "Frage",
                1,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT
        );

        List<AntwortDTO> antworten = List.of(
                new AntwortDTO(UUID.randomUUID(), "A1", frage.getFachId(), STUDENT_UUID, TIME_VAR)
        );

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(antworten.getFirst().fachId())
                .antwortText("A1")
                .frageFachId(frage.getFachId())
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);

        when(antwortService.findByFrageFachId(frage.getFachId())).thenReturn(antwort);
        when(antwortDTOMapper.toDTO(antwort)).thenReturn(antworten.getFirst());

        for (AntwortDTO a : antworten) {
            when(reviewService.getReviewByAntwortFachId(a.fachId())).thenReturn(null);
        }

        // Act
        boolean result = examManagementService.isSubmitBeingReviewed(
                EXAM_UUID, new StudentDTO(STUDENT_UUID,"Student"));

        // Assert
        assertFalse(result);
    }


    @Test
    @DisplayName("Laden des Bewertungsstatus ist erfolgreich (1 von 2 Freitext-Antworten hat Review)")
    void reviewCoverage_01() {
        // Arrange
        Frage frage1 = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .frageText("F1")
                .maxPunkte(2)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .frageText("F2")
                .maxPunkte(2)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        FrageDTO frageDTO1 = new FrageDTO(
                frage1.getFachId(),
                "F1",
                2,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        FrageDTO frageDTO2 = new FrageDTO(
                frage2.getFachId(),
                "F2",
                2,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT
        );

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .fachId(UUID.randomUUID())
                .antwortText("A1")
                .frageFachId(frage1.getFachId())
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .fachId(UUID.randomUUID())
                .antwortText("A2")
                .frageFachId(frage2.getFachId())
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        AntwortDTO antwortDTO1 = new AntwortDTO(
                antwort1.getFachId(),
                "A1",
                frageDTO1.fachId(),
                STUDENT_UUID,
                TIME_VAR);

        AntwortDTO antwortDTO2 = new AntwortDTO(
                antwort2.getFachId(),
                "A2",
                frageDTO2.fachId(),
                STUDENT_UUID,
                TIME_VAR);

        Review r1 = new Review.ReviewBuilder()
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortDTO1.fachId())
                .korrektorFachId(UUID.randomUUID())
                .bewertung("Gut")
                .punkte(1)
                .build();

        ReviewDTO r1DTO = new ReviewDTO(
                r1.getFachId(),
                antwortDTO1.fachId(),
                r1.getKorrektorFachId(),
                r1.getBewertung(),
                r1.getPunkte()
        );

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage1, frage2));

        when(antwortService.findByFrageFachId(frage1.getFachId())).thenReturn(antwort1);
        when(antwortService.findByFrageFachId(frage2.getFachId())).thenReturn(antwort2);

        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwortDTO1);
        when(antwortDTOMapper.toDTO(antwort2)).thenReturn(antwortDTO2);

        when(reviewService.getReviewByAntwortFachId(antwortDTO1.fachId())).thenReturn(r1);
        when(reviewService.getReviewByAntwortFachId(antwortDTO2.fachId())).thenReturn(null);

        when(reviewDTOMapper.toDTO(r1)).thenReturn(r1DTO);

        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);

        // Act
        double coverage = examManagementService.reviewCoverage(EXAM_UUID);

        // Assert
        assertThat(coverage).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Laden des Bewertungsstatus ist erfolgreich (1 von 1 Freitext-Antwort hat Review)")
    void reviewCoverage_02() {
        // Arrange
        Frage frage1 = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .frageText("F1")
                .maxPunkte(2)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .frageText("F2")
                .maxPunkte(2)
                .type(QuestionType.MC)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        FrageDTO frageDTO1 = new FrageDTO(
                frage1.getFachId(),
                "F1",
                2,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT
        );

        FrageDTO frageDTO2 = new FrageDTO(
                frage2.getFachId(),
                "F2",
                2,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.MC
        );

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .fachId(UUID.randomUUID())
                .antwortText("A1")
                .frageFachId(frage1.getFachId())
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .fachId(UUID.randomUUID())
                .antwortText("A2")
                .frageFachId(frage2.getFachId())
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        AntwortDTO antwortDTO1 = new AntwortDTO(
                antwort1.getFachId(),
                "A1",
                frageDTO1.fachId(),
                STUDENT_UUID,
                TIME_VAR);

        AntwortDTO antwortDTO2 = new AntwortDTO(
                antwort2.getFachId(),
                "A2",
                frageDTO2.fachId(),
                STUDENT_UUID,
                TIME_VAR);

        Review r1 = new Review.ReviewBuilder()
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortDTO1.fachId())
                .korrektorFachId(UUID.randomUUID())
                .bewertung("Gut")
                .punkte(1)
                .build();

        ReviewDTO r1DTO = new ReviewDTO(
                r1.getFachId(),
                antwortDTO1.fachId(),
                r1.getKorrektorFachId(),
                r1.getBewertung(),
                r1.getPunkte()
        );

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage1, frage2));

        when(antwortService.findByFrageFachId(frage1.getFachId())).thenReturn(antwort1);
        when(antwortService.findByFrageFachId(frage2.getFachId())).thenReturn(antwort2);

        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwortDTO1);
        when(antwortDTOMapper.toDTO(antwort2)).thenReturn(antwortDTO2);

        when(reviewService.getReviewByAntwortFachId(antwortDTO1.fachId())).thenReturn(r1);
        when(reviewService.getReviewByAntwortFachId(antwortDTO2.fachId())).thenReturn(null);

        when(reviewDTOMapper.toDTO(r1)).thenReturn(r1DTO);

        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);

        // Act
        double coverage = examManagementService.reviewCoverage(EXAM_UUID);

        // Assert
        // 2 Fragen: MC und Freitextaufgaben, aber von den Freitextaufgaben haben alle eine Bewertung
        assertThat(coverage).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Freitextantworten aller Studenten wird erfolgreich gefunden und nach Namen gruppiert")
    void getStudentSubmittedExam_01() {
        // Arrange
        UUID antwortFachId = UUID.randomUUID();
        UUID frageFachId = UUID.randomUUID();

        Student student = new Student.StudentBuilder()
                .fachId(STUDENT_UUID)
                .name(STUDENT_NAME)
                .build();

        Frage frage = new Frage.FrageBuilder()
                .fachId(frageFachId)
                .frageText("F1")
                .maxPunkte(10)
                .type(QuestionType.FREITEXT)
                .professorUUID(PROF_UUID)
                .examUUID(EXAM_UUID)
                .build();

        FrageDTO frageDTO = new FrageDTO(
                frageFachId,
                "F1",
                10,
                PROF_UUID,
                EXAM_UUID,
                QuestionTypeDTO.FREITEXT);

        StudentDTO studentDTO = new StudentDTO(STUDENT_UUID, STUDENT_NAME);

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .fachId(antwortFachId)
                .antwortText("Antwort 1")
                .frageFachId(frageFachId)
                .studentFachId(STUDENT_UUID)
                .antwortZeitpunkt(TIME_VAR)
                .build();

        AntwortDTO antwort1DTO = new AntwortDTO(
                antwortFachId,
                "Antwort 1",
                frageFachId,
                STUDENT_UUID,
                TIME_VAR);

        when(frageService.getFragenForExam(EXAM_UUID)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);

        when(antwortService.findByFrageFachId(frageFachId)).thenReturn(antwort1);
        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwort1DTO);

        when(studentService.getStudent(STUDENT_UUID)).thenReturn(student);
        when(studentDTOMapper.toDTO(student)).thenReturn(studentDTO);

        // Act
        List<StudentDTO> result = examManagementService.getStudentSubmittedExam(EXAM_UUID);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(studentDTO);
    }
}
