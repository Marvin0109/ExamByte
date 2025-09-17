package exambyte.application;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.AutomaticReviewService;
import exambyte.application.service.AutomaticReviewServiceImpl;
import exambyte.application.service.ExamManagementService;
import exambyte.application.service.ExamManagementServiceImpl;
import exambyte.domain.mapper.*;
import exambyte.domain.model.aggregate.exam.*;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.*;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.infrastructure.mapper.*;
import exambyte.infrastructure.persistence.repository.ExamRepositoryImpl;
import exambyte.infrastructure.service.*;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExamManagementServiceTest {

    private ExamService examService;
    private ExamRepository examRepository;
    private AntwortService antwortService;
    private FrageService frageService;
    private StudentService studentService;
    private ProfessorService professorService;
    private KorrektorService korrektorService;
    private KorrekteAntwortenService korrekteAntwortenService;
    private ExamDTOMapper examDTOMapper;
    private FrageDTOMapper frageDTOMapper;
    private AntwortDTOMapper antwortDTOMapper;
    private KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;
    private ReviewDTOMapper reviewDTOMapper;
    private ExamManagementService examManagementService;
    private ReviewService reviewService;
    private AutomaticReviewService automaticReviewService;

    @BeforeEach
    void setUp() {
        examService = mock(ExamServiceImpl.class);
        examRepository = mock(ExamRepositoryImpl.class);
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
        examManagementService = new ExamManagementServiceImpl(examService, antwortService, frageService, studentService,
                professorService, korrektorService, examRepository, examDTOMapper, frageDTOMapper, antwortDTOMapper,
                korrekteAntwortenService, korrekteAntwortenDTOMapper, reviewDTOMapper, reviewService,
                automaticReviewService);
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist erfolgreich")
    void test_01() {
        // Arrange
        String profName = "Prof 1";
        String title = "Exam 1";
        UUID id = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 1, 1, 0);
        LocalDateTime result = LocalDateTime.of(2020, 1, 1, 1, 0);

        Collection<Exam> exams = new ArrayList<>();
        exams.add(new Exam.ExamBuilder()
                .title(title)
                .startTime(start)
                .endTime(end)
                .resultTime(result)
                .professorFachId(id)
                .build());

        ExamDTO dto = new ExamDTO(null, null, title, id, start, end, result);

        when(professorService.getProfessorFachIdByName(profName)).thenReturn(Optional.of(id));
        when(examRepository.findAll()).thenReturn(exams);

        // Act
        boolean success = examManagementService.createExam(profName, title, start, end, result);

        // Assert
        assertThat(success).isTrue();
        assertThat(examRepository.findAll()).hasSize(1);
        verify(professorService).getProfessorFachIdByName(profName);
        verify(examDTOMapper).toDomain(dto);
        verify(examService).addExam(any());
    }

    @Test
    @DisplayName("Das erstellen eines Exams ist nicht erfolgreich")
    void test_02() {
        // Arrange
        String profName = "Prof 1";
        String title = "Exam ";
        UUID id = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 1, 1, 0);
        LocalDateTime result = LocalDateTime.of(2020, 1, 1, 1, 0);

        Collection<Exam> exams = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            exams.add(new Exam.ExamBuilder()
                    .title(title + i)
                    .startTime(start)
                    .endTime(end)
                    .resultTime(result)
                    .professorFachId(id)
                    .build());
        }

        when(professorService.getProfessorFachIdByName(profName)).thenReturn(Optional.of(id));
        when(examRepository.findAll()).thenReturn(exams);

        // Act
        boolean success = examManagementService.createExam(profName, title, start, end, result);

        // Assert
        assertThat(success).isFalse();
        assertThat(examRepository.findAll()).hasSize(12);
        verify(professorService).getProfessorFachIdByName(profName);
    }

    // TODO
    @Test
    @DisplayName("isExamAlreadySubmitted Test")
    void test_03() {}

    @Test
    @DisplayName("Exam erfolgreich gefunden")
    void test_04() {
        // Arrange
        String title = "Titel 1";
        UUID examId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 1, 1, 0);
        LocalDateTime result = LocalDateTime.of(2020, 1, 1, 1, 0);
        Exam exam = new Exam.ExamBuilder()
                .fachId(examId)
                .startTime(start)
                .endTime(end)
                .resultTime(result)
                .professorFachId(profId)
                .build();

        ExamDTO examDTO = new ExamDTO(null, examId, title, profId, start, end, result);

        when(examRepository.findByFachId(examId)).thenReturn(Optional.of(exam));
        when(examDTOMapper.toDTO(exam)).thenReturn(examDTO);

        // Act
        ExamDTO resultDTO = examManagementService.getExam(examId);

        // Assert
        assertThat(resultDTO).isNotNull();
    }

    @Test
    @DisplayName("Exam nicht erfolgreich gefunden")
    void test_05() {
        // Arrange
        UUID examId = UUID.randomUUID();

        when(examRepository.findByFachId(examId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            examManagementService.getExam(examId);
        });
        assertEquals("Exam not found", ex.getMessage());
    }

    @Test
    @DisplayName("Ein Exam wird erfolgreich gefunden nach der Startzeit")
    void test_06() {
        // Arrange
        UUID examId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);

        when(examRepository.findByStartTime(start)).thenReturn(Optional.of(examId));

        // Act
        UUID result = examManagementService.getExamByStartTime(start);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(examId);
    }

    @Test
    @DisplayName("Ein Exam wurde nicht gefunden mit der gegebenen Startzeit")
    void test_07() {
        // Arrange
        UUID examId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);

        when(examRepository.findByStartTime(start)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NichtVorhandenException.class, () -> {
            examManagementService.getExamByStartTime(start);
        });
    }

    // TODO
    @Test
    @DisplayName("createChoiceFrage Test")
    void test_08() {}

    @Test
    @DisplayName("deleteByFachId Test")
    void test_09() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        examManagementService.deleteByFachId(id);

        // Assert
        verify(examRepository).deleteByFachId(id);
    }

    @Test
    @DisplayName("reset Test")
    void test_10() {
        // Act
        examManagementService.reset();

        // Assert
        InOrder inOrder = inOrder(
                reviewService,
                antwortService,
                korrekteAntwortenService,
                frageService,
                examRepository
        );

        inOrder.verify(reviewService).deleteAll();
        inOrder.verify(antwortService).deleteAll();
        inOrder.verify(korrekteAntwortenService).deleteAll();
        inOrder.verify(frageService).deleteAll();
        inOrder.verify(examRepository).deleteAll();

        verifyNoMoreInteractions(
                reviewService,
                antwortService,
                korrekteAntwortenService,
                frageService,
                examRepository
        );
    }

    @Test
    @DisplayName("submitExam Test")
    void test_11() {
        // Arrange
        String studentLogin = "Marvin0109";
        UUID studentFachId = UUID.randomUUID();
        UUID professorFachId = UUID.randomUUID();
        Map<String, String[]> antworten = new HashMap<>();
        UUID examFachId = UUID.randomUUID();
        UUID frageFachId1 = UUID.randomUUID();
        UUID frageFachId2 = UUID.randomUUID();

        LocalDateTime antwortZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime lastChangesZeitpunkt = LocalDateTime.of(2020, 1, 1, 0, 0);

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

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .antwortText("Antwort 1")
                .frageFachId(frageFachId1)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(lastChangesZeitpunkt)
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .antwortText("Antwort 1 Antwort 2")
                .frageFachId(frageFachId2)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwortZeitpunkt)
                .lastChangesZeitpunkt(lastChangesZeitpunkt)
                .build();

        AntwortDTO antwortDTO1 = new AntwortDTO.AntwortDTOBuilder()
                .fachId(antwort1.getFachId())
                .antwortText(antwort1.getAntwortText())
                .frageFachId(antwort1.getFrageFachId())
                .studentFachId(antwort1.getStudentUUID())
                .antwortZeitpunkt(antwort1.getAntwortZeitpunkt())
                .lastChangesZeitpunkt(antwort1.getLastChangesZeitpunkt())
                .build();

        AntwortDTO antwortDTO2 = new AntwortDTO.AntwortDTOBuilder()
                .fachId(antwort2.getFachId())
                .antwortText(antwort2.getAntwortText())
                .frageFachId(antwort2.getFrageFachId())
                .studentFachId(antwort2.getStudentUUID())
                .antwortZeitpunkt(antwort2.getAntwortZeitpunkt())
                .lastChangesZeitpunkt(antwort2.getLastChangesZeitpunkt())
                .build();

        antworten.put(frageFachId1.toString(), new String[]{"Antwort 1"});
        antworten.put(frageFachId2.toString(), new String[]{"Antwort 1, Antwort 2"});

        when(studentService.getStudentFachId(studentLogin)).thenReturn(studentFachId);
        when(antwortDTOMapper.toDomain(antwortDTO1)).thenReturn(antwort1);
        when(antwortDTOMapper.toDomain(antwortDTO2)).thenReturn(antwort2);
        when(antwortDTOMapper.toDTO(antwort1)).thenReturn(antwortDTO1);
        when(antwortDTOMapper.toDTO(antwort2)).thenReturn(antwortDTO2);
        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage1, frage2));
        when(frageDTOMapper.toDTO(frage1)).thenReturn(frageDTO1);
        when(frageDTOMapper.toDTO(frage2)).thenReturn(frageDTO2);
        when(antwortService.findByStudentAndFrage(studentFachId, frageFachId1)).thenReturn(antwort1);
        when(antwortService.findByStudentAndFrage(studentFachId, frageFachId2)).thenReturn(antwort2);

        // Act
        boolean result = examManagementService.submitExam(studentLogin, antworten, examFachId);

        // Assert
        assertTrue(result);

        verify(automaticReviewService).automatischeReviewMC(any(), any(), any(), eq(studentFachId));
        verify(automaticReviewService).automatischeReviewSC(any(), any(), any(), eq(studentFachId));

        verify(antwortService, times(2)).addAntwort(any());
    }

    @Test
    @DisplayName("getAllAttempts ist erfolgreich")
    void test_12() {
        // Arrange
        UUID studentLogin = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
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

        Review review = new Review.ReviewBuilder()
                .fachId(UUID.randomUUID())
                .antwortFachId(antwortFachId)
                .korrektorFachId(korrektorFachId)
                .bewertung("Gut")
                .punkte(5)
                .build();

        ReviewDTO reviewDTO = new ReviewDTO(null, UUID.randomUUID(), antwortFachId, korrektorFachId, "Gut", 5);

        // Stubs
        when(studentService.getStudentFachId(anyString())).thenReturn(studentFachId);
        when(frageService.getFragenForExam(examFachId)).thenReturn(List.of(frage));
        when(frageDTOMapper.toDTO(frage)).thenReturn(frageDTO);
        when(antwortService.findByStudentAndFrage(studentFachId, frageFachId)).thenReturn(antwortDomain);
        when(antwortDTOMapper.toDTO(antwortDomain)).thenReturn(antwortDTO);
        when(reviewService.getReviewByAntwortFachId(antwortFachId)).thenReturn(review);
        when(reviewDTOMapper.toDTO(review)).thenReturn(reviewDTO);

        // Act
        List<VersuchDTO> attempts = examManagementService.getAllAttempts(examFachId, "testStudent");

        // Assert
        assertThat(attempts).isNotNull();
        assertThat(attempts).hasSize(1);
        assertThat(attempts.get(0).erreichtePunkte()).isEqualTo(5);
    }
}
