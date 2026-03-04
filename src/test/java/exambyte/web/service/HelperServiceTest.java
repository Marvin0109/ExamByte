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
    private static final UUID HELPER_FRAGE_ID = UUID.randomUUID();

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
            LocalDateTime start,
            LocalDateTime end,
            LocalDateTime result,
            int size
    ) {
        ExamDTO examDTO = new ExamDTO(
                UUID.randomUUID(),
                "Titel",
                null,
                start,
                end,
                result
        );

        VersuchDTO versuchDTO = new VersuchDTO(
                null,
                1.0,
                1.0,
                100.0
        );

        when(examFacadeService.getAllExams()).thenReturn(List.of(examDTO));
        when(examFacadeService.getSubmission(any(), any())).thenReturn(versuchDTO);

        List<VersuchDTO> validAttempts = helperService.getValidAttempts("Studentname");

        assertThat(validAttempts).hasSize(size);
    }

    static Stream<Arguments> examWithDiffResultTime() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2026, 1, 1, 0,0),
                        LocalDateTime.of(2026, 1, 1, 2, 0),
                        LocalDateTime.of(2026, 1, 1, 3, 0),
                        1
                ),

                Arguments.of(
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
            LocalDateTime start,
            LocalDateTime end,
            String message
    ) {
        ExamDTO examDTO = new ExamDTO(
                null,
                "Titel",
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
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        LocalDateTime.of(2026, 1, 1, 1, 0),
                        "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten."
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten."
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        LocalDateTime.of(2026, 1, 1, 12, 0),
                        "Der Test kann erst ab den "
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 1, 9, 0),
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        ""
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("inputForTimeDiff")
    void getTimeDifference(
            LocalDateTime end,
            String message
    ) {
        ExamDTO examDTO = new ExamDTO(
                null,
                "Titel",
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
                        LocalDateTime.of(2026, 1, 1, 10, 1),
                        "1 Minute"
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        "1 Stunde"
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 2, 10, 0),
                        "1 Tag"
                ),

                Arguments.of(
                        LocalDateTime.of(2026, 1, 14, 12, 5),
                        "13 Tage 2 Stunden 5 Minuten"
                ),

                // Difference between: 2026-01-01:10-00-00 and 2026-01-12:01-01-34 are 10 days, 15 hours and 34 min
                Arguments.of(
                        LocalDateTime.of(2026, 1, 12, 1, 34),
                        "10 Tage 15 Stunden 34 Minuten"
                )
        );
    }

    @ParameterizedTest(name = "{0}")
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
    void prepareFrageData_freitext() {
        FrageDTO frageDTO = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                2,
                UUID.randomUUID(),
                QuestionTypeDTO.FREITEXT
        );

        AntwortDTO antwortDTO = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frageDTO.id(),
                UUID.randomUUID(),
                null
        );

        when(examFacadeService.getAntwortForFrageAndStudent(eq(frageDTO.id()), any())).thenReturn(antwortDTO);
        when(examFacadeService.getLoesungForFrage(frageDTO.id())).thenReturn(null);

        PreparedFrageData result = helperService.prepareFrageData(frageDTO, UUID.randomUUID());

        assertThat(result.korrekteAntwortenDTO()).isNull();
        assertThat(result.frage()).isEqualTo(frageDTO);
        assertThat(result.antwort()).isEqualTo(antwortDTO);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("differentFrageType")
    void prepareFrageData_choiceFragen(
            QuestionTypeDTO type
    ) {
        FrageDTO frageDTO = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                2,
                UUID.randomUUID(),
                type
        );

        AntwortDTO antwortDTO = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frageDTO.id(),
                UUID.randomUUID(),
                null
        );

        KorrekteAntwortenDTO korrekteAntwortenDTO = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                frageDTO.id()
        );

        when(examFacadeService.getAntwortForFrageAndStudent(eq(frageDTO.id()), any())).thenReturn(antwortDTO);
        when(examFacadeService.getLoesungForFrage(frageDTO.id())).thenReturn(korrekteAntwortenDTO);

        PreparedFrageData result = helperService.prepareFrageData(frageDTO, UUID.randomUUID());

        assertThat(result.korrekteAntwortenDTO()).isNotNull();
        assertThat(result.frage()).isEqualTo(frageDTO);
        assertThat(result.antwort()).isNotNull();
    }

    static Stream<Arguments> differentFrageType() {
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
    void prepareFrageData_antwortNull() {
        FrageDTO frageDTO = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                2,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );

        KorrekteAntwortenDTO korrekteAntwortenDTO = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                frageDTO.id()
        );

        when(examFacadeService.getAntwortForFrageAndStudent(eq(frageDTO.id()), any())).thenReturn(null);
        when(examFacadeService.getLoesungForFrage(frageDTO.id())).thenReturn(korrekteAntwortenDTO);

        PreparedFrageData result = helperService.prepareFrageData(frageDTO, UUID.randomUUID());

        assertThat(result.korrekteAntwortenDTO()).isNotNull();
        assertThat(result.frage()).isEqualTo(frageDTO);
        assertThat(result.antwort()).isNull();
    }

    @Test
    void prepareReviewViewForm() {
        // Arrange
        UUID examId = UUID.randomUUID();

        ExamDTO exam = mock(ExamDTO.class);
        when(exam.title()).thenReturn("Titel");
        when(examFacadeService.getExam(any())).thenReturn(exam);

        UUID studentId = UUID.randomUUID();
        when(examFacadeService.getStudentIdByName("Student")).thenReturn(studentId);

        VersuchDTO versuch = mock(VersuchDTO.class);
        when(versuch.erreichtePunkte()).thenReturn(10.0);
        when(versuch.maxPunkte()).thenReturn(20.0);
        when(examFacadeService.getSubmission(examId, "Student")).thenReturn(versuch);

        FrageDTO frage = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                20,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );
        when(examFacadeService.getFragenForExam(examId)).thenReturn(List.of(frage));

        AntwortDTO antwortDTO = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frage.id(),
                studentId,
                null
        );
        when(examFacadeService.getAntwortForFrageAndStudent(frage.id(), studentId)).thenReturn(antwortDTO);

        KorrekteAntwortenDTO k = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                frage.id()
        );
        when(examFacadeService.getLoesungForFrage(frage.id())).thenReturn(k);

        KorrektorDTO korrektor = new KorrektorDTO(UUID.randomUUID(), "Automatischer Korrektor");
        when(examFacadeService.getReviewerById(korrektor.id())).thenReturn(korrektor);

        ReviewDTO reviewDTO = new ReviewDTO(
                UUID.randomUUID(),
                antwortDTO.id(),
                korrektor.id(),
                "Lösung: A; B",
                10
        );
        when(examFacadeService.getReviewForAntwort(antwortDTO.id())).thenReturn(reviewDTO);

        // Act
        ReviewViewForm result = helperService.prepareReviewViewForm(examId, "Student");

        // Assert
        assertThat(result.authorName()).isEmpty();
        assertThat(result.examTitle()).isEqualTo("Titel");
        assertThat(result.erreichtePunkte()).isEqualTo(10.0);
        assertThat(result.maxPunkte()).isEqualTo(20.0);
        assertThat(result.components()).hasSize(1);
    }

    @Test
    void prepareReviewViewForm_reviewNull() {
        // Arrange
        UUID examId = UUID.randomUUID();

        ExamDTO exam = mock(ExamDTO.class);
        when(exam.title()).thenReturn("Titel");
        when(examFacadeService.getExam(any())).thenReturn(exam);

        UUID studentId = UUID.randomUUID();
        when(examFacadeService.getStudentIdByName("Student")).thenReturn(studentId);

        VersuchDTO versuch = mock(VersuchDTO.class);
        when(versuch.erreichtePunkte()).thenReturn(0.0);
        when(versuch.maxPunkte()).thenReturn(20.0);
        when(examFacadeService.getSubmission(examId, "Student")).thenReturn(versuch);

        FrageDTO frage = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                20,
                UUID.randomUUID(),
                QuestionTypeDTO.FREITEXT
        );
        when(examFacadeService.getFragenForExam(examId)).thenReturn(List.of(frage));

        AntwortDTO antwortDTO = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frage.id(),
                studentId,
                null
        );
        when(examFacadeService.getAntwortForFrageAndStudent(frage.id(), studentId)).thenReturn(antwortDTO);

        KorrekteAntwortenDTO k = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                frage.id()
        );
        when(examFacadeService.getLoesungForFrage(frage.id())).thenReturn(k);

        when(examFacadeService.getReviewForAntwort(antwortDTO.id())).thenReturn(null);

        // Act
        ReviewViewForm result = helperService.prepareReviewViewForm(examId, "Student");

        // Assert
        assertThat(result.authorName()).isEmpty();
        assertThat(result.examTitle()).isEqualTo("Titel");
        assertThat(result.erreichtePunkte()).isEqualTo(0.0);
        assertThat(result.maxPunkte()).isEqualTo(20.0);
        assertThat(result.components()).hasSize(1);
    }

    @Test
    void prepareReviewViewForm_antwortNull() {
        // Arrange
        UUID examId = UUID.randomUUID();

        ExamDTO exam = mock(ExamDTO.class);
        when(exam.title()).thenReturn("Titel");
        when(examFacadeService.getExam(any())).thenReturn(exam);

        UUID studentId = UUID.randomUUID();
        when(examFacadeService.getStudentIdByName("Student")).thenReturn(studentId);

        VersuchDTO versuch = mock(VersuchDTO.class);
        when(versuch.erreichtePunkte()).thenReturn(0.0);
        when(versuch.maxPunkte()).thenReturn(20.0);
        when(examFacadeService.getSubmission(examId, "Student")).thenReturn(versuch);

        FrageDTO frage = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                20,
                UUID.randomUUID(),
                QuestionTypeDTO.FREITEXT
        );
        when(examFacadeService.getFragenForExam(examId)).thenReturn(List.of(frage));

        when(examFacadeService.getAntwortForFrageAndStudent(frage.id(), studentId)).thenReturn(null);

        when(examFacadeService.getLoesungForFrage(frage.id())).thenReturn(null);

        // Act
        ReviewViewForm result = helperService.prepareReviewViewForm(examId, "Student");

        // Assert
        assertThat(result.authorName()).isEmpty();
        assertThat(result.examTitle()).isEqualTo("Titel");
        assertThat(result.erreichtePunkte()).isEqualTo(0.0);
        assertThat(result.maxPunkte()).isEqualTo(20.0);
        assertThat(result.components()).hasSize(1);

        var element = result.components().getFirst();
        assertThat(element.review().bewertung()).isEqualTo("Keine Bewertung");
        assertThat(element.antwort().antwortText()).isEmpty();
    }

    @Test
    void fillOldDataForm_mapsFragenToOldDataForm() {
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

        FrageDTO frage = new FrageDTO(
                HELPER_FRAGE_ID,
                "Frage",
                1,
                examId,
                QuestionTypeDTO.MC
        );

        AntwortDTO antwort = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort",
                frage.id(),
                studentId,
                null
        );

        KorrekteAntwortenDTO korrekt = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "Antwort",
                "Antwort\nAntwort 2",
                frage.id()
        );

        when(examFacadeService.getExam(examId)).thenReturn(exam);
        when(examFacadeService.getStudentIdByName("Max")).thenReturn(studentId);
        when(examFacadeService.getFragenForExam(examId)).thenReturn(List.of(frage));

        when(examFacadeService.getAntwortForFrageAndStudent(frage.id(), studentId)).thenReturn(antwort);
        when(examFacadeService.getLoesungForFrage(frage.id())).thenReturn(korrekt);

        // Act
        OldDataForm result = helperService.fillOldDataForm(examId, "Max");

        // Assert
        assertThat(result.examId()).isEqualTo(examId);
        assertThat(result.examTitle()).isEqualTo("Mathe");
        assertThat(result.components()).hasSize(1);

        OldDataDTO dto = result.components().getFirst();
        assertThat(dto.fragen()).isEqualTo(frage);
        assertThat(dto.antwort()).isNotNull();
        assertThat(dto.korrekteAntworten()).isNotNull();
    }

    @Test
    void fillSubmitForm_mcWithAnswer_splitsAndTrims() {
        OldDataDTO dto = new OldDataDTO(
                new FrageDTO(
                        UUID.randomUUID(),
                        "Frage",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.MC
                ),
                null,
                new AntwortDTO(
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
                        dto.fragen().id().toString(),
                        List.of("A", "B", "C")
                );
    }

    @Test
    void fillSubmitForm_mcWithoutAnswer_returnsEmptyList() {
        OldDataDTO dto = new OldDataDTO(
                new FrageDTO(
                        UUID.randomUUID(),
                        "Frage",
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
                        dto.fragen().id().toString(),
                        List.of()
                );
    }

    @Test
    void fillSubmitForm_freitextAntwort() {
        OldDataDTO dto = new OldDataDTO(
                new FrageDTO(
                        UUID.randomUUID(),
                        "Frage",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.FREITEXT
                ),
                null,
                new AntwortDTO(
                        UUID.randomUUID(),
                        "Antwort",
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
                        dto.fragen().id().toString(),
                        List.of("Antwort")
                );
    }

    @Test
    void fillSubmitForm_freitextWithoutAntwort() {
        OldDataDTO dto = new OldDataDTO(
                new FrageDTO(
                        UUID.randomUUID(),
                        "Frage",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.FREITEXT
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
                        dto.fragen().id().toString(),
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

        UUID frage1Id = UUID.randomUUID();
        UUID frage2Id = UUID.randomUUID();

        List<FrageDTO> fragen = List.of(
                new FrageDTO(
                        frage1Id,
                        "Frage 1",
                        1,
                        UUID.randomUUID(),
                        QuestionTypeDTO.SC
                ),

                new FrageDTO(
                        frage2Id,
                        "Frage 2",
                        4,
                        UUID.randomUUID(),
                        QuestionTypeDTO.FREITEXT
                )
        );

        when(examFacadeService.getFragenForExam(any())).thenReturn(fragen);

        when(examFacadeService.getLoesungForFrage(frage1Id)).thenReturn(mock(KorrekteAntwortenDTO.class));
        when(examFacadeService.getLoesungForFrage(frage2Id)).thenReturn(null);

        // Act
        ExamViewForm result = helperService.prepareExamViewForm(UUID.randomUUID());
        List<ExamAggregateDTO> resultComponents = result.questions();

        // Assert
        assertThat(result.examTitle()).isEqualTo("Exam");
        assertThat(result.authorName()).isEqualTo("Professor");
        assertThat(result.maxPunkte()).isEqualTo(5 );

        assertThat(resultComponents).hasSize(2);
        assertThat(resultComponents.getFirst().korrekteAntworten()).isNotNull();
        assertThat(resultComponents.getLast().korrekteAntworten()).isNull();
    }
}
