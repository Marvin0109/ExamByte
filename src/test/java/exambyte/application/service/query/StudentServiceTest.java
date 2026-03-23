package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.StudentDTOMapper;
import exambyte.domain.model.user.Student;
import exambyte.domain.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StudentServiceTest {

    private StudentService studentService;
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private Student student;
    private StudentDTO studentDTO;
    private AnswerDTO answer1;
    private AnswerDTO answer2;

    @Mock
    private AnswerService answerService;

    @Mock
    private StudentRepository repository;

    @Mock
    private StudentDTOMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentServiceImpl(answerService, repository, mapper);

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
    void getStudentSubmittedExam_OneAnswer() {
        when(answerService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer1));
        when(repository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(mapper.toDTO(student)).thenReturn(studentDTO);

        List<StudentDTO> result = studentService.getStudentSubmittedExam(UUID.randomUUID());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(studentDTO);
    }

    @Test
    void getStudentSubmittedExam_TwoAnswers() {
        when(answerService.getFreeResponseAnswersForExam(any())).thenReturn(List.of(answer1, answer2));
        when(repository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(mapper.toDTO(student)).thenReturn(studentDTO);

        List<StudentDTO> result = studentService.getStudentSubmittedExam(UUID.randomUUID());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(studentDTO);
    }

    @Test
    void getStudentSubmittedExam_studentNotFound() {
        UUID examId = UUID.randomUUID();
        when(answerService.getFreeResponseAnswersForExam(examId)).thenReturn(List.of(answer1, answer2));
        when(repository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.getStudentSubmittedExam(examId));
        verify(repository).findById(STUDENT_ID);
    }
}
