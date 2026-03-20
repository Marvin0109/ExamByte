package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.domain.mapper.StudentDTOMapper;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StudentQueryServiceTest {

    private StudentQueryService studentQueryService;
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private Student student;
    private StudentDTO studentDTO;
    private AnswerDTO answer1;
    private AnswerDTO answer2;

    @Mock
    private AnswerQueryService answerQueryService;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentDTOMapper studentDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentQueryService = new StudentQueryServiceImpl(answerQueryService, studentService, studentDTOMapper);

        student = new Student.StudentBuilder()
                .id(STUDENT_ID)
                .name("StudentName")
                .build();

        studentDTO = new StudentDTO(
                STUDENT_ID,
                "StudentName");

        answer1 = new AnswerDTO(
                null,
                "Answer 1",
                UUID.randomUUID(),
                STUDENT_ID,
                LocalDateTime.of(2000, 1, 1, 0, 0));

        answer2 = new AnswerDTO(
                null,
                "Answer 2",
                UUID.randomUUID(),
                STUDENT_ID,
                LocalDateTime.of(2000, 1, 1, 0, 0));
    }

    @Test
    void getStudentSumbittedExam_OneAnswer() {
        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer1));
        when(studentService.getStudent(STUDENT_ID)).thenReturn(student);
        when(studentDTOMapper.toDTO(student)).thenReturn(studentDTO);

        List<StudentDTO> result = studentQueryService.getStudentSubmittedExam(UUID.randomUUID());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(studentDTO);
    }

    @Test
    void getStudentSumbittedExam_TwoAnswers() {
        when(answerQueryService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer1, answer2));
        when(studentService.getStudent(STUDENT_ID)).thenReturn(student);
        when(studentDTOMapper.toDTO(student)).thenReturn(studentDTO);

        List<StudentDTO> result = studentQueryService.getStudentSubmittedExam(UUID.randomUUID());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(studentDTO);
    }
}
