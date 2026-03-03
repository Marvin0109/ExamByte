package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.application.service.usecase.ExamManagementService;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.model.aggregate.user.Professor;
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
    private KorrektorRepository korrektorRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private FrageRepository frageRepository;

    @Autowired
    private KorrekteAntwortenRepository korrekteAntwortenRepository;

    @Autowired
    private AntwortRepository antwortRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ExamManagementService managementService;

    @Autowired
    private ExamControllerService examControllerService;

    private UUID frageIdSC;
    private UUID frageIdFreitext;
    private UUID examId;

    @BeforeEach
    void setUp() {
        professorRepository.save(new Professor.ProfessorBuilder().name("Professor").build());
        korrektorRepository.save(new Korrektor.KorrektorBuilder()
                .name("Automatischer Korrektor")
                .build());

        Optional<UUID> profId = examControllerService.getProfIdByName("Professor");

        assert(profId.isPresent());

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        examRepository.save(new Exam.ExamBuilder()
                .professorId(profId.get())
                .startTime(start)
                .endTime(start.plusDays(1))
                .resultTime(start.plusDays(2))
                .title("Exam")
                .build());

        examId = examControllerService.getExamUUIDByStartTime(start);

        frageRepository.save(new Frage.FrageBuilder()
                .frageText("Frage")
                .type(QuestionType.SC)
                .maxPunkte(1)
                .examId(examId)
                .build());

        frageRepository.save(new Frage.FrageBuilder()
                .frageText("Frage 2")
                .type(QuestionType.FREITEXT)
                .maxPunkte(2)
                .examId(examId)
                .build());

        Optional<UUID> frageIdFreitextLoaded = frageRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.FREITEXT))
                .map(Frage::getId)
                .findFirst();

        assert(frageIdFreitextLoaded.isPresent());
        frageIdFreitext = frageIdFreitextLoaded.get();

        Optional<UUID> frageIdSCLoaded = frageRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.SC))
                .map(Frage::getId)
                .findFirst();

        assert(frageIdSCLoaded.isPresent());
        frageIdSC = frageIdSCLoaded.get();

        korrekteAntwortenRepository.save(new KorrekteAntworten.KorrekteAntwortenBuilder()
                .frageId(frageIdSC)
                .antwortOptionen("A\nB\nC\nD")
                .loesungen("B")
                .build());
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        professorRepository.deleteAll();
        korrektorRepository.deleteAll();
    }

    @Test
    @DisplayName("500 Studenten submitten gleichzeitig")
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
                        Map<String, List<String>> answers = generateAnswers(frageIdSC, frageIdFreitext);
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
            assertThat(antwortRepository.findByStudentIdAndFrageId(
                    s.id(), frageIdFreitext)).isPresent();

            Optional<Antwort> sc = antwortRepository.findByStudentIdAndFrageId(
                    s.id(), frageIdSC);
            assertThat(sc).isPresent();

            assertThat(reviewRepository.findByAntwortId(sc.get().getId())).isNotNull();
        }
    }

    @Test
    @DisplayName("50 gleichzeitige Submits eines Studenten")
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
                        Map<String, List<String>> answers = generateAnswers(frageIdSC, frageIdFreitext);
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

        assertThat(antwortRepository.findByStudentIdAndFrageId(
                loaded.id(), frageIdFreitext)).isPresent();

        Optional<Antwort> sc = antwortRepository.findByStudentIdAndFrageId(
                loaded.id(), frageIdSC);
        assertThat(sc).isPresent();

        assertThat(reviewRepository.findByAntwortId(sc.get().getId())).isNotNull();
    }

    private Map<String, List<String>> generateAnswers(UUID frage1Id, UUID frage2Id) {
        Map<String, List<String>> answers = new HashMap<>();
        answers.put(frage1Id.toString(), List.of("A"));
        answers.put(frage2Id.toString(), List.of("Freitext"));
        return answers;
    }
}
