package exambyte.application.service.usecase;

import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.application.service.review.ReviewGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExamManagementServiceTest {

    private ExamManagementService examManagementService;

    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID EXAM_ID = UUID.randomUUID();
    private static final UUID FRAGE_ID1 = UUID.randomUUID();
    private static final UUID FRAGE_ID2 = UUID.randomUUID();

    @Mock
    private AntwortQueryService antwortQueryService;

    @Mock
    private ReviewGenerationService reviewGenerationService;

    @Mock
    private FrageQueryService frageQueryService;

    @Mock
    private ScoringService scoringService;

    @Mock
    private ProfessorQueryService professorQueryService;

    @Mock
    private StudentQueryService studentQueryService;

    @Mock
    private ExamQueryService examQueryService;

    @Mock
    private ReviewQueryService reviewQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC")
        );

        examManagementService = new ExamManagementServiceImpl(
                antwortQueryService,
                reviewGenerationService,
                frageQueryService,
                scoringService,
                professorQueryService,
                studentQueryService,
                examQueryService,
                reviewQueryService,
                fixedClock
        );
    }

    @Test
    void createExam_success() {
        // Arrange
        String profName = "ProfName";
        UUID profId = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = start.plusHours(1);
        LocalDateTime result = end.plusHours(1);

        when(professorQueryService.getProfIdByName(profName))
                .thenReturn(Optional.of(profId));

        when(examQueryService.getAllExams())
                .thenReturn(List.of());

        // Act
        String response = examManagementService.createExam(
                profName, "Titel", start, end, result
        );

        // Assert
        assertThat(response).isEmpty();
        verify(examQueryService).addExam(any(ExamDTO.class));
    }

    @Test
    void createExam_professorNotFound_throwsException() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = start.plusHours(1);
        LocalDateTime result = end.plusHours(2);
        when(professorQueryService.getProfIdByName("Prof X"))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> examManagementService.createExam(
                        "ProfName",
                        "Titel",
                        start,
                        end,
                        result
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidExamParameters")
    void createExam_invalidInput(
            String description,
            LocalDateTime start,
            LocalDateTime end,
            LocalDateTime result,
            List<ExamDTO> existingExams,
            String expectedMessage
    ) {
        // Arrange
        String profName = "ProfName";
        UUID profId = UUID.randomUUID();

        when(professorQueryService.getProfIdByName(profName)).thenReturn(Optional.of(profId));
        when(examQueryService.getAllExams()).thenReturn(existingExams);

        // Act
        String response = examManagementService.createExam(
                profName, "Titel", start, end, result
        );

        assertThat(response).isEqualTo(expectedMessage);
        verify(examQueryService, never()).addExam(any());
    }

    static Stream<Arguments> invalidExamParameters() {
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamDTO existingExamSameMinute = new ExamDTO(
                UUID.randomUUID(),
                "Titel",
                UUID.randomUUID(),
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        List<ExamDTO> twelveExams =
                IntStream.range(0, 12)
                        .mapToObj(i -> existingExamSameMinute)
                        .toList();

        return Stream.of(
                Arguments.of(
                        "Start nach End",
                        start.plusHours(2),
                        start.plusHours(1),
                        start.plusHours(3),
                        List.of(),
                        "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!"
                ),
                Arguments.of(
                        "Start gleich End",
                        start,
                        start,
                        start.plusHours(1),
                        List.of(),
                        "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!"
                ),
                Arguments.of(
                        "Result vor End",
                        start,
                        start.plusHours(2),
                        start.plusHours(1),
                        List.of(),
                        "Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!"
                ),
                Arguments.of(
                        "Maximale Anzahl Exams überschritten",
                        start,
                        start.plusHours(1),
                        start.plusHours(2),
                        twelveExams,
                        "Die maximale Kapazität von 12 Exams ist nun überschritten worden!"
                ),
                Arguments.of(
                        "Startzeit existiert bereits",
                        start,
                        start.plusHours(1),
                        start.plusHours(2),
                        List.of(existingExamSameMinute),
                        "Ein Exam mit der selben Startzeit ist schon vorhanden!"
                )
        );
    }

    @Test
    void submitExam_studentNotFound() {
        when(studentQueryService.getStudentIdByName("Max"))
                .thenThrow(new RuntimeException("Not found"));

        SubmitExamResult result = examManagementService.submitExam(
                "Max", Map.of(), UUID.randomUUID());

        assertThat(result).isEqualTo(SubmitExamResult.STUDENT_NOT_FOUND);
    }

    @Test
    void submitExam_saveAnswersFails() {
        when(studentQueryService.getStudentIdByName("Max"))
                .thenReturn(STUDENT_ID);

        when(antwortQueryService.saveAnswers(any(), any())).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> examManagementService.submitExam(
                "Max",
                Map.of("q1", List.of("A")),
                UUID.randomUUID())
        );
    }

    @Test
    void submitExam_reviewSaveFails() {
        FrageDTO frage = mock(FrageDTO.class);
        when(frage.id()).thenReturn(FRAGE_ID1);

        AntwortDTO antwort = mock(AntwortDTO.class);
        ReviewDTO review = mock(ReviewDTO.class);

        when(review.bewertung()).thenReturn("Test");
        when(review.punkte()).thenReturn(1.0);
        when(review.antwortId()).thenReturn(UUID.randomUUID());
        when(review.reviewerId()).thenReturn(UUID.randomUUID());

        when(studentQueryService.getStudentIdByName("Max"))
                .thenReturn(STUDENT_ID);

        when(antwortQueryService.saveAnswers(any(UUID.class), any()))
                .thenReturn(true);

        when(frageQueryService.getFragenForExam(EXAM_ID))
                .thenReturn(List.of(frage));

        when(antwortQueryService.findByStudentAndFrage(STUDENT_ID, FRAGE_ID1))
                .thenReturn(antwort);

        when(reviewGenerationService.generateReviews(
                eq(STUDENT_ID),
                anyList(),
                anyList()))
                .thenReturn(List.of(review));

        doThrow(new RuntimeException("DB error"))
                .when(reviewQueryService)
                .createReview(anyString(), anyDouble(), any(), any());

        SubmitExamResult result = examManagementService.submitExam(
                "Max",
                Map.of(),
                EXAM_ID
        );

        assertThat(result).isEqualTo(SubmitExamResult.REVIEW_SAVE_FAILED);
    }


    @Test
    void submitExam_success() {
        FrageDTO frage = mock(FrageDTO.class);
        when(frage.id()).thenReturn(FRAGE_ID1);

        AntwortDTO antwort = mock(AntwortDTO.class);
        ReviewDTO review = mock(ReviewDTO.class);

        // Review-Stub
        when(review.bewertung()).thenReturn("OK");
        when(review.punkte()).thenReturn(5.0);
        when(review.antwortId()).thenReturn(UUID.randomUUID());
        when(review.reviewerId()).thenReturn(UUID.randomUUID());

        when(studentQueryService.getStudentIdByName("Max")).thenReturn(STUDENT_ID);

        when(antwortQueryService.saveAnswers(any(UUID.class), any())).thenReturn(true);

        when(frageQueryService.getFragenForExam(EXAM_ID)).thenReturn(List.of(frage));

        when(antwortQueryService.findByStudentAndFrage(STUDENT_ID, FRAGE_ID1)).thenReturn(antwort);

        when(reviewGenerationService.generateReviews(eq(STUDENT_ID), anyList(), anyList()))
                .thenReturn(List.of(review));

        SubmitExamResult result = examManagementService.submitExam("Max", Map.of(), EXAM_ID);

        assertThat(result).isEqualTo(SubmitExamResult.SUCCESS);

        verify(reviewQueryService).createReview(
                review.bewertung(),
                review.punkte(),
                review.antwortId(),
                review.reviewerId()
        );
    }

    @Test
    void getSubmission_returnsCorrectAttemptDTO() {
        String studentName = "Max";

        LocalDateTime resultTime = LocalDateTime.of(2025, 1, 1, 12, 0);

        FrageDTO frage1 = mock(FrageDTO.class);
        when(frage1.maxPunkte()).thenReturn(5.0);

        FrageDTO frage2 = mock(FrageDTO.class);
        when(frage2.maxPunkte()).thenReturn(10.0);

        ExamDTO exam = mock(ExamDTO.class);

        Map<UUID, FrageDTO> frageMap = Map.of(
                FRAGE_ID1, frage1,
                FRAGE_ID2, frage2
        );

        AntwortDTO antwort1 = mock(AntwortDTO.class);
        LocalDateTime zeit1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        when(antwort1.antwortZeitpunkt()).thenReturn(zeit1);

        AntwortDTO antwort2 = mock(AntwortDTO.class);
        LocalDateTime zeit2 = LocalDateTime.of(2025, 1, 1, 11, 0);
        when(antwort2.antwortZeitpunkt()).thenReturn(zeit2);

        List<AntwortDTO> alleAntworten = List.of(antwort1, antwort2);

        when(studentQueryService.getStudentIdByName(studentName)).thenReturn(STUDENT_ID);
        when(examQueryService.getExam(EXAM_ID)).thenReturn(exam);
        when(exam.resultTime()).thenReturn(resultTime);
        when(frageQueryService.getFragenUUIDMap(EXAM_ID)).thenReturn(frageMap);
        when(antwortQueryService.getAntworten(STUDENT_ID, frageMap.keySet())).thenReturn(alleAntworten);
        when(scoringService.berechneErreichtePunkte(alleAntworten, frageMap, resultTime)).thenReturn(12.0);

        // Act
        AttemptDTO result = examManagementService.getSubmission(EXAM_ID, studentName);

        // Assert
        assertThat(result.maxPunkte()).isEqualTo(15.0);
        assertThat(result.erreichtePunkte()).isEqualTo(12.0);
        assertThat(result.prozent()).isCloseTo(80.0, within(0.0001));
        assertThat(result.lastChanges()).isEqualTo(zeit2);

        verify(studentQueryService).getStudentIdByName(studentName);
        verify(frageQueryService).getFragenUUIDMap(EXAM_ID);
        verify(antwortQueryService).getAntworten(STUDENT_ID, frageMap.keySet());
        verify(scoringService).berechneErreichtePunkte(alleAntworten, frageMap, resultTime);
    }

    @Test
    void allowedToViewReview_yes() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 12, 0);
        ExamDTO exam = new ExamDTO(
                EXAM_ID,
                "Titel",
                UUID.randomUUID(),
                start,
                start.plusHours(1),
                start.plusHours(2)
        );

        when(examQueryService.getExam(EXAM_ID)).thenReturn(exam);

        boolean result = examManagementService.allowedToViewReview(EXAM_ID);

        assertThat(result).isTrue();
    }

    @Test
    void allowedToViewReview_no() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 9, 0);
        ExamDTO exam = new ExamDTO(
                EXAM_ID,
                "Titel",
                UUID.randomUUID(),
                start,
                start.plusHours(2),
                start.plusHours(3)
        );

        when(examQueryService.getExam(EXAM_ID)).thenReturn(exam);

        boolean result = examManagementService.allowedToViewReview(EXAM_ID);

        assertThat(result).isFalse();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("examTimeParameter")
    void deleteById(
            LocalDateTime startTime,
            LocalDateTime resultTime,
            boolean result
    ) {
        ExamDTO exam = new ExamDTO(
                EXAM_ID,
                "",
                null,
                startTime,
                null,
                resultTime
        );

        when(examQueryService.getExam(EXAM_ID)).thenReturn(exam);

        boolean ans = examManagementService.deleteById(EXAM_ID);

        assertThat(ans).isEqualTo(result);
    }

    private static Stream<Arguments> examTimeParameter() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2025, 12, 12, 0, 0),
                        LocalDateTime.of(2025, 12, 12, 2, 0),
                        true
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 3, 3, 0, 0),
                        LocalDateTime.of(2026, 3, 3, 1, 0),
                        true
                ),

                Arguments.of(
                        LocalDateTime.of(2025, 12, 12, 0, 0),
                        LocalDateTime.of(2026, 1, 1, 12, 0),
                        false
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        false
                )
        );
    }

    @Test
    void resetAllExamDataCascade_fail() {
        when(examQueryService.getAllExams()).thenReturn(List.of());

        boolean result = examManagementService.resetAllExamDataCascade();

        assertThat(result).isFalse();
        verify(examQueryService, never()).resetAllExamDataCascade();
    }

    @Test
    void resetAllExamDataCascade_fail_only11Exam() {
        ExamDTO finishedExam = mock(ExamDTO.class);
        when(finishedExam.endTime()).thenReturn(LocalDateTime.of(2025, 12, 12, 0, 0));

        List<ExamDTO> examList = new ArrayList<>(Collections.nCopies(11, finishedExam));
        when(examQueryService.getAllExams()).thenReturn(examList);

        boolean result = examManagementService.resetAllExamDataCascade();

        assertThat(result).isFalse();
        verify(examQueryService, never()).resetAllExamDataCascade();
    }

    @Test
    void resetAllExamDataCascade_fail_examRunning() {
        ExamDTO finishedExam = mock(ExamDTO.class);
        when(finishedExam.endTime()).thenReturn(LocalDateTime.of(2025, 12, 12, 0, 0));

        ExamDTO examRunning = mock(ExamDTO.class);
        when(examRunning.endTime()).thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0));

        List<ExamDTO> examList = new ArrayList<>(Collections.nCopies(11, finishedExam));
        examList.add(examRunning);
        when(examQueryService.getAllExams()).thenReturn(examList);

        boolean result = examManagementService.resetAllExamDataCascade();

        assertThat(result).isFalse();
        verify(examQueryService, never()).resetAllExamDataCascade();
    }

    @Test
    void resetAllExamDataCascade_success_allExamDone() {
        ExamDTO finishedExam = mock(ExamDTO.class);
        when(finishedExam.endTime()).thenReturn(LocalDateTime.of(2025, 12, 12, 0, 0));

        List<ExamDTO> examList = new ArrayList<>(Collections.nCopies(12, finishedExam));
        when(examQueryService.getAllExams()).thenReturn(examList);

        boolean result = examManagementService.resetAllExamDataCascade();

        assertThat(result).isTrue();
        verify(examQueryService).resetAllExamDataCascade();
    }
}
