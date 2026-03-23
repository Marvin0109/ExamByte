package exambyte.application.service.query;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.mapper.ExamDTOMapper;
import exambyte.application.mapper.QuestionDTOMapper;
import exambyte.domain.model.exam.Exam;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.ExamService;
import exambyte.domain.service.QuestionService;
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

    private QuestionDTO questionDTO;
    private Question question;

    private AnswerDTO answerDTO;

    private static final LocalDateTime START = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final UUID STUDENT_ID = UUID.randomUUID();

    @Mock
    private ExamService examService;

    @Mock
    private StudentService studentService;

    @Mock
    private QuestionService questionService;

    @Mock
    private AnswerService answerService;

    @Mock
    private ExamDTOMapper examDTOMapper;

    @Mock
    private QuestionDTOMapper questionDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        examQueryService = new ExamQueryServiceImpl(
                examService,
                studentService,
                questionService,
                answerService,
                examDTOMapper,
                questionDTOMapper);

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
                .start(START)
                .end(START.plusHours(1))
                .result(START.plusHours(2))
                .build();

        exam2 = new Exam.ExamBuilder()
                .id(examDTO2.id())
                .title("Exam 2")
                .professorId(examDTO2.professorId())
                .start(START.plusHours(1))
                .end(START.plusHours(2))
                .result(START.plusHours(3))
                .build();

        questionDTO = new QuestionDTO(
                UUID.randomUUID(),
                "Question",
                10,
                exam1.getId(),
                QuestionTypeDTO.FREE_RESPONSE);

        question = new Question.FrageBuilder()
                .id(questionDTO.id())
                .text("Question")
                .points(10)
                .examId(exam1.getId())
                .type(QuestionType.FREE_RESPONSE)
                .build();

        answerDTO = new AnswerDTO(
                UUID.randomUUID(),
                "Answer",
                question.getId(),
                STUDENT_ID,
                START
        );
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
        when(studentService.getStudentIdByName("Student")).thenReturn(STUDENT_ID);
        when(questionService.getQuestionsForExam(any())).thenReturn(List.of(question));
        when(questionDTOMapper.toQuestionDTOList(any())).thenReturn(List.of(questionDTO));
        when(answerService.findByStudentAndQuestion(STUDENT_ID, question.getId())).thenReturn(answerDTO);

        boolean result = examQueryService.hasStudentSubmittedExam(exam1.getId(), "Student");

        assertTrue(result);
    }

    @Test
    void hasStudentSubmittedExam_returnsFalse() {
        when(studentService.getStudentIdByName("Student")).thenReturn(STUDENT_ID);
        when(questionService.getQuestionsForExam(any())).thenReturn(List.of(question));
        when(questionDTOMapper.toQuestionDTOList(any())).thenReturn(List.of(questionDTO));
        when(answerService.findByStudentAndQuestion(STUDENT_ID, question.getId())).thenReturn(null);

        boolean result = examQueryService.hasStudentSubmittedExam(exam1.getId(), "Student");

        assertFalse(result);
    }
}
