package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.application.service.usecase.ExamManagementService;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.repository.*;
import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainerConfiguration.class)
class SubmitAnswersStressTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CorrectAnswersRepository correctAnswersRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ExamManagementService managementService;

    @Autowired
    private ExamControllerService examControllerService;

    private UUID questionIdSC;
    private UUID questionIdFreeResponse;
    private UUID examId;

    @BeforeEach
    void setUp() {
        professorRepository.save(new Professor.ProfessorBuilder().name("Professor").build());
        reviewerRepository.save(new Reviewer.ReviewerBuilder()
                .name("Auto reviewer")
                .build());

        Optional<UUID> profId = examControllerService.getProfIdByName("Professor");

        assert(profId.isPresent());

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        examRepository.save(new Exam.ExamBuilder()
                .professorId(profId.get())
                .start(start)
                .end(start.plusDays(1))
                .result(start.plusDays(2))
                .title("Exam")
                .build());

        examId = examControllerService.getExamUUIDByStartTime(start);

        questionRepository.save(new Question.FrageBuilder()
                .text("Question")
                .type(QuestionType.SC)
                .points(1)
                .examId(examId)
                .build());

        questionRepository.save(new Question.FrageBuilder()
                .text("Question 2")
                .type(QuestionType.FREE_RESPONSE)
                .points(2)
                .examId(examId)
                .build());

        Optional<UUID> questionIdResponseLoaded = questionRepository.findAll().stream()
                .filter(q -> q.getType().equals(QuestionType.FREE_RESPONSE))
                .map(Question::getId)
                .findFirst();

        assert(questionIdResponseLoaded.isPresent());
        questionIdFreeResponse = questionIdResponseLoaded.get();

        Optional<UUID> questionIdSCLoaded = questionRepository.findAll().stream()
                .filter(q -> q.getType().equals(QuestionType.SC))
                .map(Question::getId)
                .findFirst();

        assert(questionIdSCLoaded.isPresent());
        questionIdSC = questionIdSCLoaded.get();

        correctAnswersRepository.save(new CorrectAnswers.CorrectAnswersBuilder()
                .questionId(questionIdSC)
                .choices("A\nB\nC\nD")
                .solution("B")
                .build());
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        professorRepository.deleteAll();
        reviewerRepository.deleteAll();
    }

    @Test
    @DisplayName("500 Studenten geben gleichzeitig deren Antworten ab")
    void stressTestSubmitExam() throws InterruptedException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            studentRepository.save(new Student.StudentBuilder().name("Student " + i).build());
            students.add(studentRepository.findByName("Student " + i).orElseThrow());
            assertThat(students.get(i)).isNotNull();
        }

        // Stress tests starts here
        int threadCount = students.size();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        try {
            for (Student s : students) {
                final String studentName = s.getName();
                executor.submit(() -> {
                    try {
                        Map<String, List<String>> answers = generateAnswers(questionIdSC, questionIdFreeResponse);
                        managementService.submitExam(studentName, answers, examId);
                    } catch (Exception e) {
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        assertThat(exceptions).isEmpty();

        for (Student s : students) {
            assertThat(answerRepository.findByStudentIdAndQuestionId(
                    s.id(), questionIdFreeResponse)).isPresent();

            Optional<Answer> sc = answerRepository.findByStudentIdAndQuestionId(
                    s.id(), questionIdSC);
            assertThat(sc).isPresent();

            assertThat(reviewRepository.findByAnswerId(sc.get().getId())).isNotNull();
        }
    }

    @Test
    @DisplayName("50 gleichzeitiges Einreichen eines Studenten")
    void stressTestSubmitExam_02() throws InterruptedException {
        studentRepository.save(new Student.StudentBuilder().name("Student 0").build());
        Student loaded = studentRepository.findByName("Student 0").orElseThrow();
        assertThat(loaded).isNotNull();

        // Stress tests starts here
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        try {
            for (int i = 0; i < threadCount; i++) {
                final String studentName = "Student 0";
                executor.submit(() -> {
                    try {
                        Map<String, List<String>> answers = generateAnswers(questionIdSC, questionIdFreeResponse);
                        managementService.submitExam(studentName, answers, examId);
                    } catch (Exception e) {
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        assertThat(exceptions).isEmpty();

        assertThat(answerRepository.findByStudentIdAndQuestionId(
                loaded.id(), questionIdFreeResponse)).isPresent();

        Optional<Answer> sc = answerRepository.findByStudentIdAndQuestionId(
                loaded.id(), questionIdSC);
        assertThat(sc).isPresent();

        assertThat(reviewRepository.findByAnswerId(sc.get().getId())).isNotNull();
    }

    private Map<String, List<String>> generateAnswers(UUID frage1Id, UUID frage2Id) {
        Map<String, List<String>> answers = new HashMap<>();
        answers.put(frage1Id.toString(), List.of("A"));
        answers.put(frage2Id.toString(), List.of("FreeResponse"));
        return answers;
    }
}
