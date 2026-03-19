package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.AntwortService;
import exambyte.domain.service.ExamService;
import exambyte.domain.service.FrageService;
import exambyte.domain.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExamQueryServiceTest {

    private ExamQueryService examQueryService;

    private ExamDTO examDTO1;
    private ExamDTO examDTO2;
    private Exam exam1;
    private Exam exam2;

    private FrageDTO frageDTO;
    private Frage frage;

    private Antwort antwort;

    private static final LocalDateTime START = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final UUID STUDENT_ID = UUID.randomUUID();

    @Mock
    private ExamService examService;

    @Mock
    private StudentService studentService;

    @Mock
    private FrageService frageService;

    @Mock
    private AntwortService antwortService;

    @Mock
    private ExamDTOMapper examDTOMapper;

    @Mock
    private FrageDTOMapper frageDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        examQueryService = new ExamQueryServiceImpl(
                examService,
                studentService,
                frageService,
                antwortService,
                examDTOMapper,
                frageDTOMapper);

        examDTO1 = new ExamDTO(
                UUID.randomUUID(),
                "Exam 1",
                UUID.randomUUID(),
                START,
                START.plusHours(1),
                START.plusHours(2));

        examDTO2 = new ExamDTO(
                UUID.randomUUID(),
                "Exam 2",
                UUID.randomUUID(),
                START.plusHours(1),
                START.plusHours(2),
                START.plusHours(3));

        exam1 = new Exam.ExamBuilder()
                .id(examDTO1.id())
                .title("Exam 1")
                .professorId(examDTO1.professorId())
                .startTime(START)
                .endTime(START.plusHours(1))
                .resultTime(START.plusHours(2))
                .build();

        exam2 = new Exam.ExamBuilder()
                .id(examDTO2.id())
                .title("Exam 2")
                .professorId(examDTO2.professorId())
                .startTime(START.plusHours(1))
                .endTime(START.plusHours(2))
                .resultTime(START.plusHours(3))
                .build();

        frageDTO = new FrageDTO(
                UUID.randomUUID(),
                "Frage",
                10,
                exam1.getId(),
                QuestionTypeDTO.FREE_RESPONSE);

        frage = new Frage.FrageBuilder()
                .id(frageDTO.id())
                .frageText("Frage")
                .maxPunkte(10)
                .examId(exam1.getId())
                .type(QuestionType.FREE_RESPONSE)
                .build();

        antwort = new Antwort.AntwortBuilder()
                .id(UUID.randomUUID())
                .antwortText("Antwort")
                .studentId(STUDENT_ID)
                .frageId(frage.getId())
                .antwortZeitpunkt(START)
                .build();
    }

    @Test
    void getExamIdByStartTime_returnsUUID() {
        when(examService.allExams()).thenReturn(List.of(exam1, exam2));
        when(examDTOMapper.toDTO(exam1)).thenReturn(examDTO1);
        when(examDTOMapper.toDTO(exam2)).thenReturn(examDTO2);

        UUID result = examQueryService.getExamIdByStartTime(START);

        assertEquals(exam1.getId(), result);
    }

    @Test
    void getExamIdByStartTime_returnsNull() {
        when(examService.allExams()).thenReturn(List.of());

        UUID result = examQueryService.getExamIdByStartTime(START);

        assertNull(result);
    }

    @Test
    void getAllExams_returnsSorted() {
        when(examService.allExams()).thenReturn(List.of(exam2, exam1));
        when(examDTOMapper.toDTO(exam1)).thenReturn(examDTO1);
        when(examDTOMapper.toDTO(exam2)).thenReturn(examDTO2);

        List<ExamDTO> result = examQueryService.getAllExams();

        assertEquals(List.of(examDTO1, examDTO2), result);
    }

    @Test
    void hasStudentSubmittedExam_returnsTrue() {
        when(studentService.getStudentId("Student")).thenReturn(STUDENT_ID);
        when(frageService.getFragenForExam(any())).thenReturn(List.of(frage));
        when(frageDTOMapper.toFrageDTOList(any())).thenReturn(List.of(frageDTO));
        when(antwortService.findByStudentAndFrage(STUDENT_ID, frage.getId())).thenReturn(antwort);

        boolean result = examQueryService.hasStudentSubmittedExam(exam1.getId(), "Student");

        assertTrue(result);
    }

    @Test
    void hasStudentSubmittedExam_returnsFalse() {
        when(studentService.getStudentId("Student")).thenReturn(STUDENT_ID);
        when(frageService.getFragenForExam(any())).thenReturn(List.of(frage));
        when(frageDTOMapper.toFrageDTOList(any())).thenReturn(List.of(frageDTO));
        when(antwortService.findByStudentAndFrage(STUDENT_ID, frage.getId())).thenReturn(null);

        boolean result = examQueryService.hasStudentSubmittedExam(exam1.getId(), "Student");

        assertFalse(result);
    }
}
