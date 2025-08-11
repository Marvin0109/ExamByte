package exambyte.application;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.ExamManagementService;
import exambyte.application.service.ExamManagementServiceImpl;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.*;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.infrastructure.mapper.AntwortDTOMapperImpl;
import exambyte.infrastructure.mapper.ExamDTOMapperImpl;
import exambyte.infrastructure.mapper.FrageDTOMapperImpl;
import exambyte.infrastructure.mapper.KorrekteAntwortenDTOMapperImpl;
import exambyte.infrastructure.persistence.repository.ExamRepositoryImpl;
import exambyte.infrastructure.service.*;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ExamManagementServiceTest {

    private ExamService examService;
    private ExamRepository examRepository;
    private AntwortService antwortService;
    private FrageService frageService;
    private StudentService studentService;
    private ProfessorService professorService;
    private KorrekteAntwortenService korrekteAntwortenService;
    private ExamDTOMapper examDTOMapper;
    private FrageDTOMapper frageDTOMapper;
    private AntwortDTOMapper antwortDTOMapper;
    private KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;
    private ExamManagementService examManagementService;

    @BeforeEach
    void setUp() {
        examService = mock(ExamServiceImpl.class);
        examRepository = mock(ExamRepositoryImpl.class);
        antwortService = mock(AntwortServiceImpl.class);
        frageService = mock(FrageServiceImpl.class);
        studentService = mock(StudentServiceImpl.class);
        professorService = mock(ProfessorServiceImpl.class);
        korrekteAntwortenService = mock(KorrekteAntwortenServiceImpl.class);
        examDTOMapper = mock(ExamDTOMapperImpl.class);
        frageDTOMapper = mock(FrageDTOMapperImpl.class);
        antwortDTOMapper = mock(AntwortDTOMapperImpl.class);
        korrekteAntwortenDTOMapper = mock(KorrekteAntwortenDTOMapperImpl.class);
        examManagementService = new ExamManagementServiceImpl(examService, antwortService, frageService, studentService,
                professorService, examRepository, examDTOMapper, frageDTOMapper, antwortDTOMapper, korrekteAntwortenService,
                korrekteAntwortenDTOMapper);
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

    // TODO
    @Test
    @DisplayName("submitExam Test")
    void test_04() {}

    // TODO
    @Test
    @DisplayName("Exam erfolgreich gefunden")
    void test_05() {
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
    void test_06() {
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
    void test_07() {
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
    void test_08() {
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
    void test_09() {}
}
