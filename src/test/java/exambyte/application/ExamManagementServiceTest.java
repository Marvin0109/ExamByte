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
import exambyte.infrastructure.mapper.AntwortDTOMapperImpl;
import exambyte.infrastructure.mapper.ExamDTOMapperImpl;
import exambyte.infrastructure.mapper.FrageDTOMapperImpl;
import exambyte.infrastructure.mapper.KorrekteAntwortenDTOMapperImpl;
import exambyte.infrastructure.persistence.repository.ExamRepositoryImpl;
import exambyte.infrastructure.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("Das Erstellen eines Exams ist erfolgreich")
    void test_01() {
        // Arrange
        String profName = "Professor";
        UUID profFachID = UUID.randomUUID();
        String title = "Exam 1";
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 1, 1, 0);
        LocalDateTime result = LocalDateTime.of(2020, 1, 1, 2, 0);

        Collection<Exam> exams = new ArrayList<>();
        exams.add(new Exam.ExamBuilder().professorFachId(profFachID).title(title).startTime(start)
                .endTime(end).resultTime(result).build());

        when(professorService.getProfessorFachId(profName)).thenReturn(Optional.of(profFachID));
        when(examRepository.findAll()).thenReturn(exams);

        // Act
        examManagementService.createExam(profName, title, start, end, result);

        // Assert
        verify(professorService).getProfessorFachId(profName);
        verify(examRepository).findAll();
        verify(examDTOMapper).toDomain(any(ExamDTO.class));
    }

    // TODO: Teste die restlichen Methoden
}
